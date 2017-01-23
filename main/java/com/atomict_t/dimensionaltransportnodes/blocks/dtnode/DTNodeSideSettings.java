package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.io.Serializable;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;

import com.atomict_t.dimensionaltransportnodes.utils.__;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class DTNodeSideSettings implements INBTSerializable<NBTTagCompound>{
//	public static final int MAX_SLOT_CHECKS_PER_TICK = 32;
	public static final int MAX_ITEM_PER_TICK = 4;
	private int currentSlot = 0;
	private int itemsToMove = 0;
	    
	public boolean isInput = false;
	public boolean isInputWhitelist = true;
	
	public boolean isOutput = false;
	public boolean isOutputWhitelist = true;
	
	public ArrayList<ItemStack> inputFilter = new ArrayList<>(); 
	public ArrayList<ItemStack> outputFilter = new ArrayList<>(); 
	
	public BlockPos pos;
	private TileEntity inv;
	
	public DTNodeSideSettings(){
		this(new BlockPos(0,0,0));
	}
	
	public DTNodeSideSettings(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		this.isInput = compound.getBoolean("isInput");
		this.isInputWhitelist = compound.getBoolean("isInputWhitelist");
		this.isOutput = compound.getBoolean("isOutput");
		this.isOutputWhitelist = compound.getBoolean("isOutputWhitelist");
		
		this.inputFilter = nbtToFilter(compound.getCompoundTag("inFilter"));
		this.outputFilter = nbtToFilter(compound.getCompoundTag("outFilter"));
    }
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("isInput", this.isInput);
		compound.setBoolean("isInputWhitelist", this.isInputWhitelist);
		compound.setBoolean("isOutput", this.isOutput);
		compound.setBoolean("isOutputWhitelist", this.isOutputWhitelist);
		
		compound.setTag("inFilter", filterToNBT(inputFilter));
		compound.setTag("outFilter", filterToNBT(outputFilter));
		return compound;
	}
	
	public NBTTagCompound filterToNBT(ArrayList<ItemStack> filter){
		NBTTagCompound compound = new NBTTagCompound();
	    NBTTagList list = new NBTTagList();
		
		for(ItemStack stack : filter){
			list.appendTag(stack.writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("filter", list);
		compound.setInteger("size", filter.size());
		
		return compound;
	}
	
	public ArrayList<ItemStack> nbtToFilter(NBTTagCompound nbt){
		ArrayList<ItemStack> filter = new ArrayList<>();
		
		int size = nbt.getInteger("size");
		NBTTagList nbtFilter = nbt.getTagList("filter", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < size; i++){
			filter.add(ItemStack.loadItemStackFromNBT(nbtFilter.getCompoundTagAt(i) ));
		}
		
		return filter;
	}
	
	public void onTick(World worldObj, BlockPos pos){
		this.pos = pos;
		if(isInput){
			getInventory(worldObj);
			if(inv != null){
				transferItems();
			}
		}
	}
	
	public void transferItems(){
//		__.log("Trying to transfer");
		// set number of items to move this tick
		itemsToMove = MAX_ITEM_PER_TICK;
		// Get inventory's ItemHandler
		IItemHandler itemstackHandler = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		// make a temp stack
		ItemStack stack;
		
		// go through the allowed inventory slots
		for(int i = 0; i < itemstackHandler.getSlots(); i++){
			// make sure that one a certain amount is moved each tick
			if(itemsToMove <= 0) return;
			
			// Get the stack to move;
			stack = itemstackHandler.getStackInSlot(i);

			// If there is a stack and we are allowed to move
			if(stack != null && stack.stackSize > 0 && canExtract(stack)){
				// Make a copy and try to move it
				ItemStack tempStack = stack.copy();
				if(isInputWhitelist){
					ItemStack filterStack = getInFilter(tempStack);
					if(filterStack.stackSize > 0)
						tempStack.stackSize = Math.min(tempStack.stackSize, amountPresent(tempStack) - filterStack.stackSize);
				}
				int moved = moveToTargets(tempStack);
				itemsToMove -= moved;
				// Remove the moved amount from original slot
				if(moved>0){
					itemstackHandler.extractItem(i, moved, false);
				}
			}
		}
	}
	
	public int moveToTargets(ItemStack aStack){
//		__.log("trying to move to targets");
		/***
		 *  This function is supposed to merge a stack into the output inventories
		 ***/
		// Get a copy of the stack
		ItemStack itemCopy = aStack.copy();
		// Make the stacksize the amount we are able to move at one tick
		int toMove = itemCopy.stackSize = Math.min(aStack.stackSize, MAX_ITEM_PER_TICK);
		
		// Find outputs
		for(DTNodeTileEntity te : DTNodeTileEntity.nodes){
			for(int i = 0; i < 6; i++){
				DTNodeSideSettings side = te.getSide(i);
				if(side == this) continue; // Make sure that we are not selffeeding
				
				// Are we allowed to input into inventory
				if(side.isOutput){
					// Update the inventory at that position;
					side.getInventory(te.getWorld());
					// if there isn't an inventory there, move on
					if(side.inv == null) continue;
					
					if(!side.canInsert(itemCopy)) continue;
					if(side.isOutputWhitelist){
						ItemStack filterStack = side.getOutFilter(itemCopy);
						if(filterStack.stackSize > side.amountPresent(itemCopy))
							toMove = itemCopy.stackSize = Math.min(itemCopy.stackSize, filterStack.stackSize - side.amountPresent(itemCopy));
					}
					
					// Try to merge itemstack into inventory
					int moved = mergeStack(side.inv, itemCopy);
					if(moved != 0)
						te.markDirty();
					
					itemCopy.stackSize -= moved;
					if(itemCopy.stackSize <= 0)
						return toMove;
				}
			}
		}
		
		
		// return number of moved items
		return toMove - itemCopy.stackSize;
	}
	
	private int mergeStack(TileEntity inv, ItemStack stack) {
//		__.log("trying to merge stack into inventory");
		ItemStack itemCopy = stack;
		int toMove = itemCopy.stackSize;
		IItemHandler handler =  inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		for(int i = 0; i < handler.getSlots(); i++){
			ItemStack temp = handler.getStackInSlot(i);
			
			if(temp == null) {
				// Have to insert an empty stack before inserting anything...???
				temp = itemCopy.copy();
				temp.stackSize = 0;
				handler.insertItem(i, temp, false);
				// Insert actual item
				handler.insertItem(i, itemCopy, false);
				return toMove;
			};
			
			if(temp.getItem() == itemCopy.getItem()){
				int limit = temp.getMaxStackSize() - temp.stackSize;
				if(itemCopy.stackSize < limit){
					handler.insertItem(i, itemCopy, false);
					return toMove;
				} else {
					handler.insertItem(i, itemCopy, false);
					itemCopy.stackSize -= limit;
				}
			}
		}

		return toMove - itemCopy.stackSize;
	}

	public void getInventory(World worldObj){
//		__.log("getting inventory at " + pos);
		inv = worldObj.getTileEntity(pos);
		if(inv == null) return;
//		__.log("gotten something");
		if(!inv.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			inv = null;
//		__.log(inv == null ? "not an inventory" : "got an inventory");
	}
	
	public ItemStack getInFilter(ItemStack stack){
		for(ItemStack filterStack : inputFilter){
			if(stack.getItem() == filterStack.getItem())
				return filterStack.copy();
		}
		return null;
	}
	
	private boolean canExtract(ItemStack stack){
		ItemStack filterStack = getInFilter(stack);
		if(filterStack == null){
			if(isInputWhitelist){
				return false;
			} else {
				return true;
			}
		} else {
			if(isInputWhitelist){
				if(amountPresent(filterStack) > filterStack.stackSize){
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
	}
	
	public int amountPresent(ItemStack item){
		int count = 0;
		
		IItemHandler handler =  inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		for(int i = 0; i < handler.getSlots(); i++){
			ItemStack stack = handler.getStackInSlot(i);
			if(stack == null) continue;
			
			if(stack.getItem() == item.getItem())
				count += stack.stackSize;
		}
		
		return count;
	}
	
	
	public ItemStack getOutFilter(ItemStack stack){
		for(ItemStack filterStack : outputFilter){
			if(stack.getItem() == filterStack.getItem())
				return filterStack.copy();
		}
		return null;
	}
	
	public boolean canInsert(ItemStack stack) {
		if(inv == null) return false;
		if(stack == null) return false;
		
		ItemStack filterStack = getOutFilter(stack);
		if(filterStack == null){
			if(isOutputWhitelist){
				return false;
			} else {
				return true;
			}
		} else {
			if(isOutputWhitelist){
				if(filterStack.stackSize == 0 || amountPresent(filterStack) < filterStack.stackSize){
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
	}

	
}
