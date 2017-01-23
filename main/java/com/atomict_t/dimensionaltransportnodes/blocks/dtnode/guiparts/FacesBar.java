package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiContainer;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEButton;
import com.atomict_t.dimensionaltransportnodes.gui.elements.IGuiElement;
import com.atomict_t.dimensionaltransportnodes.utils.BlockFace;
import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class FacesBar extends GuiElement {
	private GEButton[] buttons = new GEButton[6];
//	private int numButtons = 6;
//	private boolean[] enabled = new boolean[numButtons];

	private int buttonWidth = 19, buttonHeight = 19;
	
	public int activeFace = -1;
	
	public FacesBar(GuiElement parent) {
		super(parent);
		
		textureX = 176; textureY = 0;
		textureW = 19; textureH = 19;
		x = -20; y = 0;
		width = 19; height = 19 * 6;
		
//		this.parent = parent;
//		te = parent.te;
//		texture = parent.texture;
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i] = new GEButton(this);
			buttons[i].y = i * buttonHeight;
			buttons[i].setDimentions(buttonWidth, buttonHeight);
			
			buttons[i].setDefault(textureX, textureY + i * textureH, textureW, textureH);
			buttons[i].setActive(textureX + textureW, textureY + i * textureH, textureW, textureH);
			buttons[i].setDisabled(textureX + 2 * textureW, textureY + i * textureH, textureW, textureH);
			
			if(((DTNodeTileEntity)te).hasInventory(i)){
				if(activeFace < 0){
					activeFace = i;
					buttons[i].active = true;
				}
			} else {
				buttons[i].enabled = false;
			}
		}
	}
	
	public void render(Minecraft mc, int mouseX, int mouseY){
		if(!visible) return;
		
		mc.getTextureManager().bindTexture(texture);
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].render(mc, mouseX, mouseY);
		}
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
			for(int i = 0; i < buttons.length; i++){
				if(buttons[i].mouseClicked(mouseX, mouseY, mouseButton)){
					buttons[activeFace].active = false;
					activeFace = i;
					buttons[activeFace].active = true;
					return true;
				}
			}
		return false;
	}
	
//	@Override
//	public int getX() {
//		return parent.getX() + x;
//	}
//	
//	@Override
//	public int getY() {
//		return parent.getY() + y;
//	}
}
