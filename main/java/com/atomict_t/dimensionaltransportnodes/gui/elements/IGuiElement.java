package com.atomict_t.dimensionaltransportnodes.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public interface IGuiElement {
	public IGuiElement parent = null;
	public TileEntity te = null;
	
	// position relative to parent.
	public int x = 0;
	public int y = 0;
	public int width = 10;
	public int height = 10;
	
	public ResourceLocation texture = null;
	public int textureX = 0;
	public int textureY = 0;
	public int textureW = 10;
	public int textureH = 10;
	
	public boolean visible = true;
	public boolean enabled = true;
	public boolean active = false;

	public void render(Minecraft mc, int mouseX, int mouseY);
	
    public boolean mousePressed(int mouseX, int mouseY);
	
    public boolean mouseOver(int mx, int my);
}
