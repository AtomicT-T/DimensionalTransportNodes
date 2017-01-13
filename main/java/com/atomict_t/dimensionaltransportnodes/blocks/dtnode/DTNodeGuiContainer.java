package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.io.IOException;

import com.atomict_t.dimensionaltransportnodes.Config;
import com.atomict_t.dimensionaltransportnodes.DTNPacketHandler;
import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.DTNodeSideSettingsGui;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.FacesBar;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.packets.DTNodeTileEntityPacket;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GUITap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

public class DTNodeGuiContainer extends GuiContainer{
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;
    
    private static final ResourceLocation background = new ResourceLocation(Config.modId, "textures/gui/gui.png");

    private DTNodeTileEntity te;
    private static FacesBar fBar;
    private DTNodeSideSettingsGui settingsGui;
    
    public DTNodeGuiContainer(DTNodeTileEntity te, DTNodeContainer container) {
        super(container);

        this.te = te;
        
        xSize = WIDTH;
        ySize = HEIGHT;
        
        fBar = new FacesBar(te, background);
        settingsGui = new DTNodeSideSettingsGui(te, fBar, background);
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 20, xSize, ySize);
        
        fBar.render(guiLeft - 20, guiTop);
        settingsGui.render(guiLeft + 3, guiTop + 3);
        
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	if(fBar.mouseClicked(mouseX, mouseY, mouseButton)){
    		// face bar clicked
    	} else if(settingsGui.mouseClicked(mouseX, mouseY, mouseButton)){
    		DTNPacketHandler.INSTANCE.sendToAll(new DTNodeTileEntityPacket(te.getPos(), te.serializeNBT()));
    		
			te.markDirty();

        	IBlockState state = te.getWorld().getBlockState(te.getPos());
        	te.getWorld().notifyBlockUpdate(te.getPos(), state, state,3);
    	}
    }
    
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
