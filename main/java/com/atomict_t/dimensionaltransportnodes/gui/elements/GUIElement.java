package com.atomict_t.dimensionaltransportnodes.gui.elements;

import net.minecraft.client.Minecraft;

public class GUIElement {
	public int x = 0;
	public int y = 0;
	public int width = 10;
	public int height = 10;
	
	public boolean visible = true;
	public boolean enabled = true;
	public boolean active = false;
	
	private int id;
	
	public GUIElement(int id, int x, int y, int width, int height){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public GUIElement(int id, int width, int height){
		this(id, 0, 0, width, height);
	}
	
	public void render(Minecraft mc, int mouseX, int mouseY){
	}
	
    public boolean mousePressed(int mouseX, int mouseY)
    {
        return this.enabled && this.visible && this.mouseOver(mouseX, mouseY);
    }
    
    public boolean mouseOver(int mouseX, int mouseY)
    {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }
}
