package com.atomict_t.dimensionaltransportnodes.gui.elements;

import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiElement extends Gui implements IGuiElement {
	public GuiElement parent = null;
	public TileEntity te;
	
	// position relative to parent.
	public int x = 0;
	public int y = 0;
	public int width = 10;
	public int height = 10;
	
	public ResourceLocation texture;
	public int textureX = 0;
	public int textureY = 0;
	public int textureW = 10;
	public int textureH = 10;
	
	public boolean visible = true;
	public boolean enabled = true;
	public boolean active = false;
	
	public GuiElement[] elements;
	
	public GuiElement(GuiElement parent){
		this.parent = parent;
		this.te = parent.te;
		texture = parent.texture;
	}
	
	public GuiElement(TileEntity te, ResourceLocation texture){
		this.te = te;
		this.texture = texture;
	}
		
	public void render(Minecraft mc, int mouseX, int mouseY){
        mc.getTextureManager().bindTexture(texture);
		if(enabled && visible){
			drawTexturedModalRect(
//			drawModalRectWithCustomSizedTexture(
					getX(), getY(), 
					this.textureX, this.textureY,
//					this.width, this.height,
					this.textureW, this.textureH
				);
			if(elements != null)
				for(GuiElement el : elements)
					el.render(mc, mouseX, mouseY);
		}
	}
	
	public int getX(){
//		__.log(" getX: " + (parent.getX() + x));
		if(parent != null)
			return parent.getX() + x;
		return x;
	}
	
	public int getY(){
//		__.log(" getY: " + (parent.getY() + y));
		if(parent != null)
			return parent.getY() + y;
		return y;
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
		return mousePressed(mouseX, mouseY);
	}
	
    public boolean mousePressed(int mouseX, int mouseY)
    {
        if(this.enabled && this.visible && this.mouseOver(mouseX, mouseY))
        	return true;
        
		if(elements != null)
			for(GuiElement el : elements)
				if(el.mousePressed(mouseX, mouseY))
					return true;
		
        return false;
    }
    
    public boolean mouseOver(int mx, int my)
    {
        return mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
    }
    
    @Override
    public String toString() {
    	return "GuiElement[" + getX() + ", " + getY() + ", " + width + ", " + height + "]";
    }
}
