package com.atomict_t.dimensionaltransportnodes.utils;

import net.minecraft.util.EnumFacing;

public class BlockFace {
	public static final int North = 0;
	public static final int South = 1;
	public static final int East = 2;
	public static final int West = 3;
	public static final int Up = 4;
	public static final int Down = 5;
	
	public static EnumFacing toMCFacing(int face){
		switch(face){
			case 0:
				return EnumFacing.NORTH;
			case 1:
				return EnumFacing.SOUTH;
			case 2:
				return EnumFacing.EAST;
			case 3:
				return EnumFacing.WEST;
			case 4:
				return EnumFacing.UP;
			case 5:
				return EnumFacing.DOWN;
		}
		return null;
	}
	
	public static int toBlockFace(EnumFacing face){
		if(face == null) return -1;
		switch(face){
			case NORTH:
				return 0;
			case SOUTH:
				return 1;
			case EAST:
				return 2;
			case WEST:
				return 3;
			case UP:
				return 4;
			case DOWN:
				return 5;
		}
		return -1;
	}
	
	public static int getOpposite(int face){
		switch(face){
			case North: return South;
			case South: return North;
			case East: return West;
			case West: return East;
			case Up: return Down;
			case Down: return Up;
		}
		return -1;
	}
}
