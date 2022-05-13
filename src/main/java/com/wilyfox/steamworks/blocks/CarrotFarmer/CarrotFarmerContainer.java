package com.wilyfox.steamworks.blocks.CarrotFarmer;

import com.wilyfox.steamworks.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class CarrotFarmerContainer extends Container {
    private final IInventory inventory;
    private IIntArray fields;

    public CarrotFarmerContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new CarrotFarmerTileEntity(), new IntArray(buffer.readByte()));
    }

    public CarrotFarmerContainer(int id , PlayerInventory playerInventory, IInventory inventory, IIntArray fields) {
        super(ModContainers.CARROT_FARMER.get(), id);
        this.inventory = inventory;
        this.fields = fields;

        // Input Slots
        this.addSlot(new Slot(this.inventory, 0, 44, 18));
        this.addSlot(new Slot(this.inventory, 1, 62, 18));
        this.addSlot(new Slot(this.inventory, 2, 44, 36));
        this.addSlot(new Slot(this.inventory, 3, 62, 36));

        // Output slots
        this.addSlot(new Slot(this.inventory, 4, 98, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(this.inventory, 5, 116, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(this.inventory, 6, 98, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new Slot(this.inventory, 7, 116, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // Player inv
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                int idx = x + y * 9;
                int posX = 8 + x * 18;
                int posY = 48 + 19 + y * 18;
                this.addSlot(new Slot(playerInventory, idx + 9, posX, posY));
            }
        }

        // Player hotbar
        for (int x = 0; x < 9; ++x) {
            int idx = x;
            int posX = 8 + x * 18;
            int posY = 106 + 19;
            this.addSlot(new Slot(playerInventory, idx, posX, posY));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 8;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index < 8) {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index > 7) {
                if (!this.moveItemStackTo(itemstack1, 0, 7, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
