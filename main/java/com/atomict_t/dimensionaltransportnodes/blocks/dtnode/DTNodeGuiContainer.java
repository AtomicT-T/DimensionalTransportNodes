package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.io.IOException;

import javax.swing.text.IconView;

import com.atomict_t.dimensionaltransportnodes.Config;
import com.atomict_t.dimensionaltransportnodes.DTNPacketHandler;
import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.DTNodeSideSettingsGui;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.FacesBar;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.packets.DTNodeTileEntityPacket;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GUITap;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;
import com.atomict_t.dimensionaltransportnodes.gui.elements.IGuiElement;
import com.atomict_t.dimensionaltransportnodes.utils.__;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import scala.swing.event.MouseClicked;

public class DTNodeGuiContainer extends GuiContainer{
	DTNodeGuiWrapper wrapper;
	
	public static DTNodeContainer container;
	
    public DTNodeGuiContainer(DTNodeTileEntity te, DTNodeContainer container) {
        super(container);
        this.container = container;
        
        wrapper = new DTNodeGuiWrapper(te, new ResourceLocation(Config.modId, "textures/gui/gui.png"));
        xSize = wrapper.width;
        ySize = wrapper.height;
	}

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        drawDefaultBackground();
        
    	wrapper.x = guiLeft;
    	wrapper.y = guiTop;
    	wrapper.render(mc, mouseX, mouseY);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	wrapper.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
