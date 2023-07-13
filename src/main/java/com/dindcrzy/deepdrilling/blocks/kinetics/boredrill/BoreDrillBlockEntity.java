package com.dindcrzy.deepdrilling.blocks.kinetics.boredrill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.dindcrzy.deepdrilling.blocks.ore_node.OreNodeBlockEntity;
import com.dindcrzy.deepdrilling.coreloot.DDCoreLoot;
import com.dindcrzy.deepdrilling.Helper;
import com.dindcrzy.deepdrilling.blocks.DDBlockEntities;
import com.dindcrzy.deepdrilling.blocks.DDBlockTags;
import com.dindcrzy.deepdrilling.blocks.DDBlocks;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitBlock;
import com.dindcrzy.deepdrilling.blocks.kinetics.drillbit.BaseBitBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.BaseModuleBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.collection.CollectionBlockEntity;
import com.dindcrzy.deepdrilling.blocks.kinetics.modules.efficiency.OverclockBlockEntity;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlock;

import com.tterrag.registrate.util.entry.BlockEntityEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

// too different from other BlockBreaking entities
public class BoreDrillBlockEntity extends KineticBlockEntity {
	public static final AtomicInteger NEXT_BREAKER_ID = BlockBreakingKineticBlockEntity.NEXT_BREAKER_ID;
	private final int breakerId = -NEXT_BREAKER_ID.incrementAndGet(); // idk what this does but it seems important
	private int ticksUntilNextProgress;
	private int destroyProgress; // number that's -1 (not
	private BlockPos breakingPos; // stored position of "cosmetic breaking", right in front of the drill
	private BlockPos corePos; // stored position of actual core block being targeted, a distance from the drill
	private final HashMap<BlockPos, BaseModuleBlockEntity> modules = new HashMap<>();
	public BoreDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Override
	public void onSpeedChanged(float previousSpeed) {
		super.onSpeedChanged(previousSpeed);
		if (destroyProgress == -1) {
			destroyNextTick();
		}
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (ticksUntilNextProgress == -1)
			destroyNextTick();
	}

	public void destroyNextTick() {
		ticksUntilNextProgress = 1;
	}

	public void reset() {
		assert level != null;
		destroyProgress = 0;
		ticksUntilNextProgress = -1;
		if (breakingPos != null)
			level.destroyBlockProgress(breakerId, breakingPos, -1);
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		compound.putInt("Progress", destroyProgress);
		compound.putInt("NextTick", ticksUntilNextProgress);
		if (breakingPos != null)
			compound.put("Breaking", NbtUtils.writeBlockPos(breakingPos));
		if (corePos != null)
			compound.put("Core", NbtUtils.writeBlockPos(corePos));
		if (modules.size() > 0) {
			ListTag nbtModules = new ListTag();
			for (BlockPos modulePos : modules.keySet()) {
				nbtModules.add(NbtUtils.writeBlockPos(modulePos));
			}
			compound.put("Modules", nbtModules);
		}
		super.write(compound, clientPacket);
	}

	private boolean initialisedModules = false;
	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		destroyProgress = compound.getInt("Progress");
		ticksUntilNextProgress = compound.getInt("NextTick");
		if (compound.contains("Breaking"))
			breakingPos = NbtUtils.readBlockPos(compound.getCompound("Breaking"));
		if (compound.contains("Core"))
			corePos = NbtUtils.readBlockPos(compound.getCompound("Core"));
		initialisedModules = false;
		if (compound.contains("Modules")) {
			ListTag nbtModules = compound.getList("Modules", ListTag.TAG_COMPOUND);
			for (int i = 0; i < nbtModules.size(); i++) {
				// world isnt loaded enough to get block entities yet
				modules.put(NbtUtils.readBlockPos(nbtModules.getCompound(i)), null);
			}
		}
		super.read(compound, clientPacket);
	}

	@Nullable
	public OreNodeBlockEntity getNodeBlockEntity() {
		assert level != null;
		BlockPos pos = getCorePos();
		if (pos != null && level.getBlockEntity(pos) instanceof OreNodeBlockEntity oreNodeBlockEntity) {
			return oreNodeBlockEntity;
		}
		return null;
	}

	@Nullable
	public BaseBitBlockEntity getBitBlockEntity() {
		assert level != null;
		BlockPos pos = getDrillBitPos();
		if (level.getBlockEntity(pos) instanceof BaseBitBlockEntity baseBitBlockEntity) {
			return baseBitBlockEntity;
		}
		return null;
	}

	@Override
	public void invalidate() { // called usually when a block is destroyed or chunk unloaded
		super.invalidate();
		assert level != null;
		if (!level.isClientSide && destroyProgress != 0)
			level.destroyBlockProgress(breakerId, breakingPos, -1);
		for (BaseModuleBlockEntity moduleBlockEntity : modules.values()) {
			moduleBlockEntity.clearDrill();
		}
	}

	private BlockPos getBreakingPos() {
		return getBlockPos().relative(getBlockState().getValue(DrillBlock.FACING), 2);
	}

	private BlockPos getDrillBitPos() {
		return getBlockPos().relative(getBlockState().getValue(DrillBlock.FACING));
	}

	private static final int searchW = 3;
	private static final int searchH = 4;
	@Nullable
	public BlockPos getCorePos() {
		if (corePos != null && canReach(breakingPos, corePos)) {
			return corePos;
		}

		Direction dir = getBlockState().getValue(DrillBlock.FACING);
		BlockPos searchCenter = getBlockPos().relative(dir, searchH - 1);
		BoundingBox box = switch (dir.getAxis()) {
			case X -> new BoundingBox(
					searchCenter.getX() - searchH, searchCenter.getY() - searchW, searchCenter.getZ() - searchW,
					searchCenter.getX() + searchH, searchCenter.getY() + searchW, searchCenter.getZ() + searchW
			);
			case Y -> new BoundingBox(
					searchCenter.getX() - searchW, searchCenter.getY() - searchH, searchCenter.getZ() - searchW,
					searchCenter.getX() + searchW, searchCenter.getY() + searchH, searchCenter.getZ() + searchW
			);
			// case Z
			default -> new BoundingBox(
					searchCenter.getX() - searchW, searchCenter.getY() - searchW, searchCenter.getZ() - searchH,
					searchCenter.getX() + searchW, searchCenter.getY() + searchW, searchCenter.getZ() + searchH
			);
		};

		for (Iterator<BlockPos> it = BlockPos.MutableBlockPos.betweenClosedStream(box).iterator(); it.hasNext(); ) {
			BlockPos to = it.next();
			if (canDrill(to) && canReach(breakingPos, to))
				return to;
		}

		return null;
	}

	private boolean hasDrill() {
		assert level != null;
		return level.getBlockState(getDrillBitPos()).getBlock() instanceof BaseBitBlock<?>;
	}

	public boolean canDrill(BlockPos to) {
		assert level != null;
		return level.getBlockState(to).is(DDBlocks.ORE_NODE.get());
	}

	public boolean canReach(BlockPos from, BlockPos to) {
		assert level != null;
		if (!hasDrill() || !canDrill(to)) {
			return false;
		}
		ClipBlockStateContext ctx = new ClipBlockStateContext(
				Helper.asVec3(from),
				Helper.asVec3(to),
				b -> !b.is(DDBlockTags.DRILL_THROUGH)
		);
		return level.isBlockInLine(ctx).getType() == HitResult.Type.MISS;
	}

	@Override
	public void tick() {
		assert level != null;
		if (!initialisedModules) {
			initialisedModules = true;
			for (BlockPos pos : modules.keySet()) {
				BaseModuleBlockEntity moduleBlockEntity = modules.get(pos);
				if (moduleBlockEntity == null &&
						level.getBlockEntity(pos) instanceof BaseModuleBlockEntity worldModuleBlockEntity) {
					modules.put(pos, worldModuleBlockEntity);
				}
			}
			checkListedModules();
		}
		super.tick();
		if (level instanceof ServerLevel serverLevel && getSpeed() != 0 && ticksUntilNextProgress >= 0) {
			if (hasDrill()) {
				ticksUntilNextProgress--;
				if (corePos != null && ticksUntilNextProgress % 3 == 0) {
					level.playSound(null, worldPosition,
							level.getBlockState(corePos).getSoundType().getHitSound(),
							SoundSource.NEUTRAL, .25f, 1);
					OreNodeBlockEntity oreNode = getNodeBlockEntity();
					if (oreNode != null) {
						Helper.particlePlane(serverLevel,
								Helper.asVec3(getBlockPos()).relative(getBlockState().getValue(DrillBlock.FACING), 1.5f),
								getBlockState().getValue(DrillBlock.FACING).getAxis(),
								new BlockParticleOption(ParticleTypes.BLOCK, oreNode.getReferenceModel()),
								5
						);
					}
				}
				if (ticksUntilNextProgress <= 0) {
					breakStage();
				}
			} else {
				reset();
			}
		}
	}

	private double getBreakSpeed() {
		double cumulativeSpeed = Math.abs(getSpeed());
		for (Iterator<BaseModuleBlockEntity> it = modules.values().stream()
				.filter(p -> p instanceof OverclockBlockEntity).iterator(); it.hasNext(); ) {
			OverclockBlockEntity overclockBlockEntity = (OverclockBlockEntity)it.next();
			cumulativeSpeed += Math.abs(overclockBlockEntity.getSpeed());
		}
		if (cumulativeSpeed < 256)
			return Math.sqrt(cumulativeSpeed / 256f);
		else
			return cumulativeSpeed / 256f;
	}

	private void breakStage() {
		checkListedModules();
		breakingPos = getBreakingPos();
		corePos = getCorePos();
		assert level != null;
		if (corePos == null) {
			if (destroyProgress != 0) { // was breaking core but can no longer find it
				reset();
			}
		} else {
			double breakSpeed = getBreakSpeed();
			destroyProgress++;
			if (destroyProgress >= 10) { // finished destroying
				level.playSound(null, worldPosition,
						level.getBlockState(corePos).getSoundType().getHitSound(),
						SoundSource.BLOCKS, 1f, 1);
				onBreakComplete();
				reset();
			} else {
				ticksUntilNextProgress = (int) (20 / breakSpeed);
				level.destroyBlockProgress(breakerId, breakingPos, destroyProgress);
			}
		}
	}

	public int getBitDamageApplied() {
		int d = 1;
		d += getModulesOfType(DDBlockEntities.DRILL_OVERCLOCK).size();
		return d;
	}

	public Optional<BaseModuleBlockEntity> getFirstModuleOfType(BlockEntityEntry<? extends BaseModuleBlockEntity> entry) {
		return modules.values().stream().filter(entry::is).findFirst();
	}

	public List<BaseModuleBlockEntity> getModulesOfType(BlockEntityEntry<? extends BaseModuleBlockEntity> entry) {
		return modules.values().stream().filter(entry::is).toList();
	}

	private List<ItemStack> getOutputs(ServerLevel serverLevel, BaseBitBlockEntity bitBlockEntity, OreNodeBlockEntity oreNodeBlockEntity) {
		LootContext.Builder builder = new LootContext.Builder(serverLevel)
				.withRandom(serverLevel.random)
				.withParameter(DDCoreLoot.DRILL_TYPE_CTX, Registry.BLOCK.getKey(bitBlockEntity.getBlockState().getBlock()))
				.withParameter(DDCoreLoot.DRILL_DAMAGE_CTX, (float)bitBlockEntity.getDamage() / bitBlockEntity.getDurability());
		LootTable lootTable = serverLevel.getServer().getLootTables().get(oreNodeBlockEntity.getLoot());
		return lootTable.getRandomItems(builder.create(DDCoreLoot.CORE));
	}

	private void onBreakComplete() {
		BaseBitBlockEntity bitBlockEntity = getBitBlockEntity();
		OreNodeBlockEntity oreNodeBlockEntity = getNodeBlockEntity();
		if (level instanceof ServerLevel serverLevel && bitBlockEntity != null && oreNodeBlockEntity != null) {

			bitBlockEntity.addDamage(getBitDamageApplied());
			Optional<BaseModuleBlockEntity> opt = getFirstModuleOfType(DDBlockEntities.DRILL_COLLECTION);
			List<ItemStack> outputs = getOutputs(serverLevel, bitBlockEntity, oreNodeBlockEntity);

			for (ItemStack out : outputs) {
				if (opt.isPresent()) {
					CollectionBlockEntity collectionBlockEntity = (CollectionBlockEntity) opt.get();
					long leftover = collectionBlockEntity.tryQuickStack(out);
					out.setCount((int) leftover);
				}
				if (!out.isEmpty()) {
					Vec3 vec = Vec3.atCenterOf(getBlockPos());
					ItemEntity item = new ItemEntity(level, vec.x, vec.y, vec.z, out);
					item.setDefaultPickUpDelay();
					item.setDeltaMovement(Vec3.ZERO);
					level.addFreshEntity(item);
				}
			}
		}
	}

	public void addModule(BaseModuleBlockEntity moduleBlockEntity) {
		modules.put(moduleBlockEntity.getBlockPos(), moduleBlockEntity);
	}
	public void removeModule(BlockPos pos) {
		modules.remove(pos);
	}

	public void checkListedModules() {
		ArrayList<BlockPos> toRemove = new ArrayList<>();
		for (BlockPos pos : modules.keySet()) {
			BaseModuleBlockEntity moduleBlockEntity = modules.get(pos);
			if (moduleBlockEntity == null || !Objects.equals(moduleBlockEntity.findConnectedDrill(), this)) {
				toRemove.add(pos); // concurrent modification exception :I
			}
		}
		toRemove.forEach(this::removeModule);
	}
}
