package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.utils.BlockFace;

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
	
	public EnumFacing activeFace = null;
	
	public FacesBar(DTNodeTileEntity te, ResourceLocation background) {
		this.te = te;
		this.background = background;
		
		int first = -1;
		
		for(int i = 0; i < numButtons; i++){
			enabled[i] = te.hasInventory(i);
			if(enabled[i] && first == -1){
				first = i;
				activeFace = BlockFace.toMCFacing(i);
			}
		}
	}
	
	
	public int currentFace(){
		return BlockFace.toBlockFace(activeFace);
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
				posY + buttonHeight * currentFace(), 
				textureX + buttonWidth,
				textureY + buttonHeight * currentFace(), 
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
						
						activeFace = BlockFace.toMCFacing(i);
						return true;
					}
			}
		return false;
	}
}
