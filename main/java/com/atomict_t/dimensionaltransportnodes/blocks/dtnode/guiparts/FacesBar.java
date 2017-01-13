package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class FacesBar extends Gui {
	private DTNodeTileEntity te;
	private boolean visible = true;
//	private boolean enabled = true; // skal virke for hver knap.
	
	private ResourceLocation background;
	private int 
		textureX = 176, textureY = 0,
		posX = 0, posY = 0,
		buttonWidth = 19, buttonHeight = 19;
	
	private int numButtons = 6;
	private boolean[] enabled = new boolean[numButtons];
	
	public enum FaceType {
		N, S, E, W, U, D
	};
//	public FaceType activeFace = null;
	
	public EnumFacing activeFace = null;
	
	public FacesBar(DTNodeTileEntity te, ResourceLocation background) {
		this.te = te;
		this.background = background;
		
		int first = -1;
		
		for(int i = 0; i < numButtons; i++){
			enabled[i] = te.hasInventory(i);
			if(enabled[i] && first == -1){
				first = i;
				activeFace = EnumFacing.getFront(i);
			}
		}
	}
	
	public int face2int(EnumFacing face){
		if(face == null) return -1;
		switch(face){
			case NORTH: return 0;
			case SOUTH: return 1;
			case EAST: return 2;
			case WEST: return 3;
			case UP: return 4;
			case DOWN: return 5;
		}
		return -1; // Never happens
	}
	
	public int face2int(){
		return face2int(activeFace);
	}
	
	public EnumFacing int2face(int face){
		switch(face){
			case 0: return EnumFacing.NORTH;
			case 1: return EnumFacing.SOUTH;
			case 2: return EnumFacing.EAST;
			case 3: return EnumFacing.WEST;
			case 4: return EnumFacing.UP;
			case 5: return EnumFacing.DOWN;
		}
		return null; // Never happens
	}

	public void render(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		
		if(!visible) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(background);
		
		drawTexturedModalRect(posX, posY, textureX, textureY, buttonWidth, 6 * buttonHeight);
		
		if(activeFace != null)
			drawTexturedModalRect(
				posX, 
				posY + buttonHeight * face2int(), 
				textureX + buttonWidth,
				textureY + buttonHeight * face2int(), 
				buttonWidth, buttonHeight);
		
		for(int i = 0; i < numButtons; i++){
			if(!enabled[i])
				drawTexturedModalRect(
						posX, 
						posY + buttonHeight * i, 
						textureX + 2 * buttonWidth,
						textureY + buttonHeight * i, 
						buttonWidth, buttonHeight);
		}
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
//		if(enabled)
			for(int i = 0; i < this.numButtons; i++){
				if(mouseX >= posX && mouseX < posX + buttonWidth)
					if(mouseY >= posY + i * buttonHeight && mouseY < posY + (i + 1) * buttonHeight){
						if(!enabled[i])
							return false;
						
						activeFace = int2face(i);
						return true;
					}
			}
		return false;
	}
}
