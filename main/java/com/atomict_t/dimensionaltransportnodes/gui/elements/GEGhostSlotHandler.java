package com.atomict_t.dimensionaltransportnodes.gui.elements;

import java.util.ArrayList;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiWrapper;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeSideSettings;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.DTNodeSideSettingsGui;
import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import scala.reflect.internal.Trees.New;

public class GEGhostSlotHandler extends GuiElement {

	ArrayList<GEGhostSlot> ghostslots = new ArrayList<GEGhostSlot>();
	public ArrayList<ItemStack> itemList;
	
	private int maxRows = 3;
	private int maxCols = 9;
	
	private int startRow = 0;
	
	private GuiElement buttonUp = new GuiElement(this){
		@Override
		public void render(Minecraft mc, int mouseX, int mouseY) {
			if(!visible) return;
			
			width = 8;
			height = 6;
			x = parent.width - width;
			
//			drawRect(getX(), getY(), getX() + width, getY() + height, 0xffff0000);
			GlStateManager.color(1, 1, 1, 1);
			
			mc.getTextureManager().bindTexture(texture);
			drawTexturedModalRect(getX(), getY(), 0, 30, width, height);
		}
	};
	
	private GuiElement buttonDown = new GuiElement(this){
		@Override
		public void render(Minecraft mc, int mouseX, int mouseY) {
			if(!visible) return;
			
			width = 8;
			height = 6;
			x = parent.width - width;
			y = buttonUp.height;
			
//			drawRect(getX(), getY(), getX() + width, getY() + height, 0xffff0000);
			GlStateManager.color(1, 1, 1, 1);
			
			mc.getTextureManager().bindTexture(texture);
			drawTexturedModalRect(getX(), getY(), 0, 36, width, height);
		}
	};
	
	public GEGhostSlotHandler(GuiElement parent, ArrayList<ItemStack> itemList) {
		super(parent);
		
		this.itemList = itemList;
		for(ItemStack stack : itemList){
			ghostslots.add(new GEGhostSlot(this, stack));
		}
		ghostslots.add(new GEGhostSlot(this));
		
		width = maxCols * 18 + buttonUp.width;
		height = maxRows * 18;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(buttonUp.visible && buttonUp.mouseClicked(mouseX, mouseY, mouseButton)){
			startRow++;
		}
		if(buttonDown.visible && buttonDown.mouseClicked(mouseX, mouseY, mouseButton)){
			startRow--;
		}

		for(int i = 0; i < ghostslots.size(); i++){
			GEGhostSlot slot = ghostslots.get(i);
			if(slot.mouseClicked(mouseX, mouseY, mouseButton)){
				
				ItemStack heldItem = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();
				
				if(slot.theStack == null){
					if(heldItem != null){
						tryToInsert(heldItem);
//						slot.theStack = heldItem.copy();
//						slot.theStack.stackSize = 0;
					}
				} else {
					switch(mouseButton){
						case 0:
							if(GuiScreen.isShiftKeyDown()){
								slot.theStack.stackSize += 10;
//								itemList.get(i).stackSize += 10;
							} else {
								slot.theStack.stackSize++;
//								itemList.get(i).stackSize ++;
							}
							break;
						case 1:
							int toRemove = GuiScreen.isShiftKeyDown() ? 10 : 1;
							slot.theStack.stackSize -= toRemove;
//							itemList.get(i).stackSize -= toRemove;
							break;
					}
				}
				
//				if(slot != ghostslots.get(ghostslots.size() - 1)){
				if(slot.theStack != null){
					if(slot.theStack.stackSize < 0){
						itemList.remove(slot.theStack);
						ghostslots.remove(slot);
						
					}
				} else {
//					if(slot.theStack != null){
						tryToInsert(slot.theStack);
//						if(hasItem(slot.theStack)){
//							slot.theStack = null;
//						} else {
//							itemList.add(slot.theStack);
//							ghostslots.add(new GEGhostSlot(this));
//						}
//					}
				}
				
				return true;
			}
		}
		return false;
//		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void render(Minecraft mc, int mouseX, int mouseY) {
		drawHorizontalLine(getX(), getX() + maxCols * 18, getY(), 0xff000000);
		drawHorizontalLine(getX(), getX() + maxCols * 18, getY() + maxRows * 18, 0xff000000);
		drawVerticalLine(getX(), getY(), getY() + maxRows * 18, 0xff000000);
		drawVerticalLine(getX() + maxCols * 18, getY(), getY() + maxRows * 18, 0xff000000);
		GlStateManager.color(1,1,1,1);
		
		
		int col = 0;
		int row = startRow;
		for(int i = 0; i < (ghostslots.size() - startRow * maxCols); i++){
			GEGhostSlot slot = ghostslots.get(row * maxCols + col);
			slot.x = col * slot.width + 1;
			slot.y = (row - startRow) * slot.height + 1;
			slot.render(mc, mouseX, mouseY);
			
			col++;
			if(col >= maxCols){
				row++;
				col = 0;
			}
			if(row - startRow >= maxRows)
				break;
		}
		
		buttonDown.visible = startRow > 0;
		buttonUp.visible = ghostslots.size() / maxCols - startRow > maxRows;
		
		buttonUp.render(mc, mouseX, mouseY);
		buttonDown.render(mc, mouseX, mouseY);
	}

	public boolean hasItem(ItemStack stack){
		if(stack == null) return false;
		
		for(GEGhostSlot slot : ghostslots)
			if(slot.theStack != null && stack != slot.theStack && slot.theStack.getItem() == stack.getItem() && slot.theStack.getMetadata() == stack.getMetadata())
				return true;
		
		return false;
	}
	
	public boolean tryToInsert(ItemStack stack) {
		if(stack == null) return false;
		
		if(!hasItem(stack)){
			GEGhostSlot temp = ghostslots.get(ghostslots.size() - 1);
			temp.theStack = stack.copy();
			temp.theStack.stackSize = 0;
			itemList.add(temp.theStack);
			ghostslots.add(new GEGhostSlot(this));
			    		
    		return true;
		}
		return false;
	}
}
