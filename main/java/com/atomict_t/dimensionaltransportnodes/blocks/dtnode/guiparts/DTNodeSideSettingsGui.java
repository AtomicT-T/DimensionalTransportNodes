package com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts;

import org.omg.CORBA.Current;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiContainer;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiWrapper;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GuiElement;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEGhostSlot;
import com.atomict_t.dimensionaltransportnodes.gui.elements.GEGhostSlotHandler;
import com.atomict_t.dimensionaltransportnodes.utils.__;
import com.sun.jna.platform.dnd.GhostedDragImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class DTNodeSideSettingsGui extends GuiElement {
	private InOutToggle inOutButton;
	private InOutFilterToggle inOutFilterToggle;
	private BWToggle bwToggle;
	private GEGhostSlotHandler gSlotHandler;
	
	public DTNodeSideSettingsGui(GuiElement parent) {
		super(parent);
		
		x = 3;
		y = 3;
		width = parent.width - x;
		height = parent.height - y;
		
		this.inOutButton = new InOutToggle(this);
		
		this.inOutFilterToggle = new InOutFilterToggle(this);
		
//		this.height = gSlot.getY() + height - getY();
		this.bwToggle = new BWToggle(this);
		bwToggle.y = inOutFilterToggle.y - 3;
		bwToggle.x = width - bwToggle.width - 7;
	}
	
	public void render(Minecraft mc, int mouseX, int mouseY){
		mc.getTextureManager().bindTexture(texture);
		
		DTNodeSideSettings current = ((DTNodeTileEntity)te).getSide(((DTNodeGuiWrapper)parent).fBar.activeFace);
		
		if(current == null) return;
		
		this.inOutButton.render(mc, mouseX, mouseY);
		
		if(!current.isInput && !current.isOutput) return;
		
		this.inOutFilterToggle.render(mc, mouseX, mouseY);
		
		if(inOutFilterToggle.activeFilter == 0){
			bwToggle.isWhiteList = current.isInputWhitelist;
			bwToggle.render(mc, mouseX, mouseY);
		}
		
		if(inOutFilterToggle.activeFilter == 1){
			bwToggle.isWhiteList = current.isOutputWhitelist;
			bwToggle.render(mc, mouseX, mouseY);
		}
		
		if(gSlotHandler == null){
			changeFilter();
			
			if(gSlotHandler == null) return;
		}
		
		this.gSlotHandler.render(mc, mouseX, mouseY);
		
//		int xTrack = 20;
//		if(current.isInput){
//			inputBW.render(posX + xTrack, posY);
//			xTrack += 20;
//		}
//		
//		if(current.isOutput){
//			outputBW.render(posX + xTrack, posY);
//		}
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		DTNodeSideSettings current = ((DTNodeTileEntity)te).getSide(((DTNodeGuiWrapper)parent).fBar.activeFace);
		
		if(current == null) return false;
		
		if(inOutButton.mouseClicked(mouseX, mouseY, mouseButton)){
			inOutFilterToggle = new InOutFilterToggle(this);
			changeFilter();
			return true;
		}
		
		if(inOutFilterToggle.mouseClicked(mouseX, mouseY, mouseButton)){
			changeFilter();
			return true;
		}
		
		if(inOutFilterToggle.activeFilter > -1){
			if(bwToggle.mouseClicked(mouseX, mouseY, mouseButton)){
				if(inOutFilterToggle.activeFilter == 0){
					current.isInputWhitelist = !current.isInputWhitelist;
				}
				if(inOutFilterToggle.activeFilter == 1){
					current.isOutputWhitelist = !current.isOutputWhitelist;
				}
				return true;
			}
		}
		
		if(gSlotHandler == null) return false;
		
		if(gSlotHandler.mouseClicked(mouseX, mouseY, mouseButton)){
			if(inOutFilterToggle.activeFilter == 0){
				current.inputFilter = gSlotHandler.itemList;
			}
			if(inOutFilterToggle.activeFilter == 1){
				current.outputFilter = gSlotHandler.itemList;
			}
			
			
			return true;
		} else if(GuiScreen.isShiftKeyDown()){
    		Slot slot = DTNodeGuiWrapper.playerInvGui.getSlot(mouseX, mouseY);
    		if(slot != null){
    			if(gSlotHandler.tryToInsert(slot.getStack())){
    				if(inOutFilterToggle.activeFilter == 0){
    					current.inputFilter = gSlotHandler.itemList;
    				}
    				if(inOutFilterToggle.activeFilter == 1){
    					current.outputFilter = gSlotHandler.itemList;
    				}
    				return true;
    			}
    		}
    	}
    	
//		DTNodeSideSettings current = te.getSide(fBar.activeFace);
//		if(current == null) return false;
//		
//		if(current.isInput && inputBW.mouseClicked(mouseX, mouseY, mouseButton)){
//			return true;
//		}
//		if(current.isOutput && outputBW.mouseClicked(mouseX, mouseY, mouseButton)){
//			return true;
//		}
		
		return false;
	}
	
	public void changeFilter(){
		gSlotHandler = null;
		
		if(inOutFilterToggle.activeFilter == 0){
			this.gSlotHandler = new GEGhostSlotHandler(this, ((DTNodeTileEntity)te).getSide(DTNodeGuiWrapper.fBar.activeFace).inputFilter);
		} 
		
		if(inOutFilterToggle.activeFilter == 1){
			this.gSlotHandler = new GEGhostSlotHandler(this, ((DTNodeTileEntity)te).getSide(DTNodeGuiWrapper.fBar.activeFace).outputFilter);
		} 
		
		if(gSlotHandler != null)
			gSlotHandler.y = inOutFilterToggle.height + 28;
	}
}
