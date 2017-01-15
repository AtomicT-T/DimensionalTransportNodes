package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class InOutToggle extends Gui{

	private DTNodeTileEntity te;
	private ResourceLocation spritesheet;
	private int posX;
	private int posY;
	private int buttonWidth = 19;
	private int buttonHeight = 19;
	private FacesBar fBar;

	public InOutToggle(DTNodeTileEntity te, FacesBar fBar, ResourceLocation spritesheet) {
		this.te = te;
		this.fBar = fBar;
		this.spritesheet = spritesheet;
	}

	public void render(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(this.spritesheet);
		
		DTNodeSideSettings current = te.getSide(fBar.currentFace());
		
		if(current == null) return;
		
		int textureNum = (current.isInput ? 1 : 0) + (current.isOutput ? 2 : 0);
		
		drawTexturedModalRect(posX, posY, 233, textureNum * buttonHeight, buttonWidth, buttonHeight);
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(mouseX >= posX && mouseX < posX + buttonWidth)
			if(mouseY >= posY && mouseY < posY + buttonHeight){
				DTNodeSideSettings current = te.getSide(fBar.currentFace());
				if(current.isInput){
					current.isInput = false;
					if(current.isOutput){
						current.isOutput = false; // disable both
					} else {
						current.isOutput = true; // set to only output
					}
				} else {
					current.isInput = true;
				}
//				System.out.println("input:"+(current.isInput?"true":"false"));
				return true;
			}
		return false;
	}
}
