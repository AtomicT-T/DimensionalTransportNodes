package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class InputBWListToggle extends Gui{

	private DTNodeTileEntity te;
	private ResourceLocation spritesheet;
	private int posX;
	private int posY;
	private int buttonWidth = 19;
	private int buttonHeight = 19;
	private FacesBar fBar;

	public InputBWListToggle(DTNodeTileEntity te, FacesBar fBar, ResourceLocation spritesheet) {
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
		int textureNum = current.isInputWhitelist ? 0 : 1;
		
		drawTexturedModalRect(posX, posY, 233, 76 + textureNum * buttonHeight, buttonWidth, buttonHeight);
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(mouseX >= posX && mouseX < posX + buttonWidth)
			if(mouseY >= posY && mouseY < posY + buttonHeight){
				
				DTNodeSideSettings current = te.getSide(fBar.currentFace());
				current.isInputWhitelist = !current.isInputWhitelist;
				
				return true;
			}
		return false;
	}
}
