package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class BWToggle extends GuiElement{

	public boolean isWhiteList = true;
	
	public BWToggle(GuiElement parent) {
		super(parent);
		
		width = 19;
		height = 19;
	}

	public void render(Minecraft mc, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(texture);
		
//		DTNodeSideSettings current = te.getSide(fBar.activeFace);
//		if(current == null) return;
//		int textureNum = current.isInputWhitelist ? 0 : 1;
		
		drawTexturedModalRect(getX(), getY(), 233, 76 + (isWhiteList ? 0 : height), width, height);
	}
	
//	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
//		if(mouseX >= posX && mouseX < posX + buttonWidth)
//			if(mouseY >= posY && mouseY < posY + buttonHeight){
//				
//				DTNodeSideSettings current = te.getSide(fBar.activeFace);
//				if(current == null) return false;
//				current.isInputWhitelist = !current.isInputWhitelist;
//				
//				return true;
//			}
//		return false;
//	}
}
