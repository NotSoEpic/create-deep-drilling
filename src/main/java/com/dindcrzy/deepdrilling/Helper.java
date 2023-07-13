package com.dindcrzy.deepdrilling;

import com.google.common.collect.AbstractIterator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

public class Helper {
	public static Vec3 asVec3(BlockPos pos) {
		return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}

	public static void particlePlane(ServerLevel level, Vec3 pos, Direction.Axis normal, ParticleOptions options, int count) {
		Direction perp1 = switch (normal) {
			case X -> Direction.SOUTH;
			case Y -> Direction.EAST;
			case Z -> Direction.UP;
		};
		Direction perp2 = switch (normal) {
			case X -> Direction.UP;
			case Y -> Direction.SOUTH;
			case Z -> Direction.EAST;
		};
		Vec3 posOff = Vec3.ZERO
				.relative(perp1, 0.5)
				.relative(perp2, 0.5);
		level.sendParticles(options, pos.x, pos.y, pos.z, count, posOff.x, posOff.y, posOff.z, 0.2);
	}
}
