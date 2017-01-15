package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class DTNodeSideSettingsGui extends Gui {
	private DTNodeTileEntity te;
	private FacesBar fBar;
	
	private int posX = 0, posY = 0;
	private ResourceLocation spritesheet;
	private InOutToggle inOutButton;
	private OutputBWListToggle outputBW;
	private InputBWListToggle inputBW;
	
	public DTNodeSideSettingsGui(DTNodeTileEntity te, FacesBar fBar, ResourceLocation spritesheet) {
		this.te = te;
		this.fBar = fBar;
		this.spritesheet = spritesheet;
		
		this.inOutButton = new InOutToggle(te, fBar, spritesheet);
		this.inputBW = new InputBWListToggle(te, fBar, spritesheet);
		this.outputBW = new OutputBWListToggle(te, fBar, spritesheet);
	}
	
	public void render(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(spritesheet);
		
		DTNodeSideSettings current = te.getSide(fBar.currentFace());
		
		if(current == null) return;
		
		this.inOutButton.render(posX, posY);
		
		int xTrack = 20;
		if(current.isInput){
			inputBW.render(posX + xTrack, posY);
			xTrack += 20;
		}
		
		if(current.isOutput){
			outputBW.render(posX + xTrack, posY);
		}
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(inOutButton.mouseClicked(mouseX, mouseY, mouseButton)){
			return true;
		}
		DTNodeSideSettings current = te.getSide(fBar.currentFace());
		if(current == null) return false;
		
		if(current.isInput){
			return inputBW.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if(current.isOutput){
			return outputBW.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		return false;
	}
}
