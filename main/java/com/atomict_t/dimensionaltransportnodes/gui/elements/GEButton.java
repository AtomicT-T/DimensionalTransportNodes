package com.atomict_t.dimensionaltransportnodes.gui.elements;

import org.lwjgl.opengl.GL11;

import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.client.Minecraft;
import scala.swing.event.MouseClicked;
import scala.swing.event.MouseExited;

public class GEButton extends GuiElement {

	GuiElement[] elements = new GuiElement[3];
	
	public GEButton(GuiElement parent) {
		super(parent);
		
		for(int i = 0; i < elements.length; i++){
			elements[i] = new GuiElement(this);
		}
		
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		if(!enabled){
			elements[2].render(mc, mouseX, mouseY);
		} else if(active){
			elements[1].render(mc, mouseX, mouseY);
		} else {
			elements[0].render(mc, mouseX, mouseY);
		}
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY) {
		if(enabled && mouseOver(mouseX, mouseY)){
			return true;
		}
		return false;
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return mousePressed(mouseX, mouseY);
	}
	
	public void setDefault(int tx, int ty, int tw, int th){
		GuiElement temp = elements[0] = new GuiElement(this);
		temp.textureX = tx;
		temp.textureY = ty;
		temp.textureW = tw;
		temp.textureH = th;
	}
	
	public void setActive(int tx, int ty, int tw, int th){
		GuiElement temp = elements[1] = new GuiElement(this);
		temp.textureX = tx;
		temp.textureY = ty;
		temp.textureW = tw;
		temp.textureH = th;
	}
	
	public void setDisabled(int tx, int ty, int tw, int th){
		GuiElement temp = elements[2] = new GuiElement(this);
		temp.textureX = tx;
		temp.textureY = ty;
		temp.textureW = tw;
		temp.textureH = th;
	}
	
	public void setDimentions(int width, int height){
		this.width = width;
		this.height = height;
		for(int i = 0; i < elements.length; i++){
			elements[i].width = width;
			elements[i].height = height;
		}
	}
}
