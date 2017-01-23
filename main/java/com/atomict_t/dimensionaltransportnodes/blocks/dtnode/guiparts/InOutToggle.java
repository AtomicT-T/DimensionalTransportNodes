package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiWrapper;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class InOutToggle extends GuiElement{

	private int buttonWidth = 19;
	private int buttonHeight = 19;

	public InOutToggle(GuiElement parent) {
		super(parent);
		
		width = height = 19;
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(texture);
		
		if(parent == null) return;
		
		int face = DTNodeGuiWrapper.fBar.activeFace;
		if(face < 0) return;
		
		DTNodeSideSettings current = ((DTNodeTileEntity)te).getSide(face);
		if(current == null) return;
		
		int textureNum = (current.isInput ? 1 : 0) + (current.isOutput ? 2 : 0);
		
		drawTexturedModalRect(getX(), getY(), 233, textureNum * buttonHeight, buttonWidth, buttonHeight);
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(mouseOver(mouseX, mouseY)){
			
			int face = DTNodeGuiWrapper.fBar.activeFace;
			if(face < 0) return false;
			
			DTNodeSideSettings current = ((DTNodeTileEntity)te).getSide(face);
			if(current == null) return false;
			
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
