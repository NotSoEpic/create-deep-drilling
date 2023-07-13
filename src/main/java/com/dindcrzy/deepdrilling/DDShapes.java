package com.dindcrzy.deepdrilling;

import com.simibubi.create.AllShapes;

import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DDShapes {
	public static final VoxelShaper
		BORE_DRILL_BASE = shape(-3, 0, -3, 19, 14, 19).forDirectional()
	;

	private static AllShapes.Builder shape(VoxelShape shape) {
		return new AllShapes.Builder(shape);
	}

	private static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
		return shape(cuboid(x1, y1, z1, x2, y2, z2));
	}

	private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Block.box(x1, y1, z1, x2, y2, z2);
	}
}
