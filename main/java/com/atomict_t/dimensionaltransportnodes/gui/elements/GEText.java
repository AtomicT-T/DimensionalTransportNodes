package com.atomict_t.dimensionaltransportnodes.gui.elements;

import net.minecraft.client.Minecraft;

public class GEText extends GuiElement {
	public String text = "";
	private int color = 0x000000;
	private int hoverColor = 0x000000;
	private int activeColor = 0x000000;
	
	public GEText(GuiElement parent, String text) {
		super(parent);
		setText(text);
	}
	
	public void setText(String text){
		this.text = text;
		height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
		width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
	}
	
	public void setColor(int color, int hoverColor, int activeColor){
		this.color = color;
		this.hoverColor = hoverColor;
		this.activeColor = activeColor;
	}
	
	public void setColor(int color, int activeColor){
		setColor(color, color, activeColor);
	}
	
	public void setColor(int color){
		setColor(color, color, color);
	}
	
	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		int colorChoice = active ? activeColor : color;
		if(!active && mouseOver(mouseX, mouseY))
			colorChoice = hoverColor;
		
		mc.fontRendererObj.drawString(text, getX(), getY(), colorChoice);
	}
}
