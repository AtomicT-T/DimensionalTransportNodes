package com.atomict_t.dimensionaltransportnodes.gui.elements;

import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;

public class GEGhostSlot extends GuiElement {

	public ItemStack theStack;
	
	
	private RenderItem itemRender;

	public GEGhostSlot(GuiElement parent) {
		super(parent);
		
		width = 18; height = 18;
		textureX = 11; textureY = 21;
		itemRender = Minecraft.getMinecraft().getRenderItem();
	}
	
	public GEGhostSlot(GuiElement parent, ItemStack stack) {
		this(parent);
		theStack = stack;
	}
	
	

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(mouseOver(mouseX, mouseY)) return true;
		
		return false;
	}
	
	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
        //RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(getX(), getY(), textureX, textureY, width, height);
		if(mouseOver(mouseX, mouseY) || active)
			drawRect(getX() + 1, getY() + 1, getX() + width - 1, getY() + height - 1, -2130706433);
		if(theStack != null){
			
			IBakedModel model = itemRender.getItemModelWithOverrides(theStack, mc.theWorld, mc.thePlayer);
			int offset = (width - model.getParticleTexture().getIconWidth())/2;
			itemRender.renderItemAndEffectIntoGUI(theStack, getX() + offset, getY() + offset);
			if(theStack.stackSize > 0)
				itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, theStack, getX(), getY(), "" + theStack.stackSize);
			
		}
		
        //RenderHelper.enableStandardItemLighting();
	}
}
