package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DTNodeContainer extends Container{

	private DTNodeTileEntity te;
	
	public DTNodeContainer(IInventory playerInventory, DTNodeTileEntity te) {
//	    super(new TileEntityDTNode(playerInv, te));

	    this.te = te;

//        addOwnSlots();
        addPlayerSlots(playerInventory);
//	    this.xSize = 176;
//	    this.ySize = 166;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18;
            int y = 58 + 70;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x = 9;
        int y = 6;

        System.out.println(itemHandler.getSlots());
        // Add our own slots
        int slotIndex = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
            slotIndex++;
            x += 18;
        }
    }
    
    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

//            if (index < DTNodeTileEntity.SIZE) {
//                if (!this.mergeItemStack(itemstack1, DTNodeTileEntity.SIZE, this.inventorySlots.size(), false)) {
//                    return null;
//                }
//            } else if (!this.mergeItemStack(itemstack1, 0, DTNodeTileEntity.SIZE, false)) {
//                return null;
//            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }
}
