package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.io.IOException;

import com.atomict_t.dimensionaltransportnodes.DTNPacketHandler;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.DTNodeSideSettingsGui;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.FacesBar;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.packets.DTNodeTileEntityPacket;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEGhostSlotHandler;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEPlayerInventory;
import com.atomict_t.dimensionaltransportnodes.utils.__;
import com.sun.jna.platform.dnd.GhostedDragImage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class DTNodeGuiWrapper extends GuiElement {
//	private DTNodeGuiContainer baseGui;
	
	public static FacesBar fBar;
	private DTNodeSideSettingsGui settingsGui;
	public static GEPlayerInventory playerInvGui;
    
	public DTNodeGuiWrapper(DTNodeTileEntity te, ResourceLocation texture) {
		super(te, texture);
//		baseGui = dtNodeGuiContainer;
		
		width = 180;
		height = 200;
        fBar = new FacesBar(this);
        settingsGui = new DTNodeSideSettingsGui(this);
        playerInvGui = new GEPlayerInventory(this);
        playerInvGui.x = (int)((width - playerInvGui.width) / 2);
        playerInvGui.y = x + height - playerInvGui.height;
	}

	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
        // Drawing background
        mc.getTextureManager().bindTexture(texture);
		drawRect(getX() + 3, getY() + 3, getX() + width - 3, getY() + height - 3, 0xffc6c6c6);
        GlStateManager.color(1, 1, 1, 1);
        
        // sides
        for(int i = 4; i < width - 4; i += 3){
            drawTexturedModalRect(getX() + i, getY(), 4, 20, 3, 4);
            drawTexturedModalRect(getX() + i, getY() + height - 3, 3, 27, 3, 4);
        }
        
        for(int i = 4; i < height - 4; i += 3){
            drawTexturedModalRect(getX(), getY() + i, 0, 24, 4, 3);
            drawTexturedModalRect(getX() + width - 3, getY() + i, 7, 23, 4, 3);
        }
        
        // corners
        drawTexturedModalRect(getX(), getY(), 0, 20, 4, 4);
        drawTexturedModalRect(getX() + width - 4, getY(), 6, 20, 4, 4);
        drawTexturedModalRect(getX(), getY() + height - 4, 0, 26, 4, 4);
        drawTexturedModalRect(getX() + width - 4, getY() + height - 4, 6, 26, 4, 4);
        
        
		fBar.y = (height - fBar.height) / 2;
        fBar.render(mc, mouseX, mouseY);
        settingsGui.render(mc, mouseX, mouseY);
        
        playerInvGui.render(mc, mouseX, mouseY);
	}
	
    @Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	if(fBar.mouseClicked(mouseX, mouseY, mouseButton)){
    		// face bar clicked
    		settingsGui = new DTNodeSideSettingsGui(this);
    		return true;
    	} else if(settingsGui.mouseClicked(mouseX, mouseY, mouseButton)){
    		
    		DTNodeTileEntity.sync(te);
    		
			te.markDirty();

        	IBlockState state = te.getWorld().getBlockState(te.getPos());
        	te.getWorld().notifyBlockUpdate(te.getPos(), state, state,3);
        	
        	return true;
    	}
    	
    	return false;
    }
}
