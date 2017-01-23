package com.atomict_t.dimensionaltransportnodes.gui.elements;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;

public class GEPlayerInventory extends GuiElement {

	private int marginBottom = 7;
	
	public GEPlayerInventory(GuiElement parent) {
		super(parent);
		
		width = 161;
		height = 75;
		textureX = 11;
		textureY = 21;
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		// Remember the origin is relative
		DTNodeGuiContainer.container.setOrigin(
				x, 
				y - marginBottom
		);
		
        mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(getX(), getY() - marginBottom, textureX, textureY, width, height);
	}

	public Slot getSlot(int mouseX, int mouseY) {
		mouseX += x - getX();
		mouseY += y - getY();
		for(Slot slot : DTNodeGuiContainer.container.inventorySlots)
			if(
				mouseX >= slot.xDisplayPosition && 
				mouseX < slot.xDisplayPosition + 18 && 
				mouseY >= slot.yDisplayPosition && 
				mouseY < slot.yDisplayPosition + 18
			)
			return slot;
		return null;
	}
}
