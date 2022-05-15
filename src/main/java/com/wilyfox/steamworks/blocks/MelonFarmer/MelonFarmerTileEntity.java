package com.wilyfox.steamworks.blocks.MelonFarmer;

import com.wilyfox.steamworks.setup.ModTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.Random;

public class MelonFarmerTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    static final int WORK_TIME = 2 * 20;
    private static final int FREQUENCY_TICKS = 20;
    static final int PUMPKIN_RANGE = 1;

    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers;

    private int progress = 0;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index){
                case 0:
                    return progress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public MelonFarmerTileEntity() {
        super(ModTileEntityTypes.MELON_FARMER.get());
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(8, ItemStack.EMPTY);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick() {
        World world = this.level;
        assert world != null;
        Random rand = new Random();

        if (this.items.get(4).getCount() >= 64 && this.items.get(5).getCount() >= 64 && this.items.get(6).getCount() >= 64 && this.items.get(7).getCount() >= 64) return;

        for (int x = -2; x <= 2 ; x++) {
            for (int y = -2; y <= 2; y++) {
                BlockPos pos = this.worldPosition;
                BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + y);

                if (world.getBlockState(newPos) == Blocks.AIR.defaultBlockState() && world.getBlockState(newPos.below()).getBlock() == Blocks.FARMLAND && (world.getBlockState(newPos.east()).getBlock() == Blocks.AIR && world.getBlockState(newPos.west()).getBlock() == Blocks.AIR)) {
                    for (int i = 0; i < 4; i++) {
                        if (this.items.get(i).getItem() == Items.MELON_SEEDS) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.MELON_STEM.defaultBlockState());
                            break;
                        }
                    }
                }
            }
        }

        for (int x = -2 - PUMPKIN_RANGE; x <= 2 + PUMPKIN_RANGE ; x++) {
            for (int y = -2 - PUMPKIN_RANGE; y <= 2 + PUMPKIN_RANGE; y++) {
                BlockPos pos = this.worldPosition;
                BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + y);
                BlockState pumpkinState = world.getBlockState(newPos);
                if (pumpkinState.getBlock() == Blocks.MELON) {
                    for (int i = 0; i < 8; i++) {
                        if (this.items.get(i).getItem() == Items.MELON_SLICE && this.items.get(i).getCount() < 64) {
                            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
                            this.items.get(i).setCount(this.items.get(i).getCount() + 3 + rand.nextInt(4));
                            if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
                            world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                            break;
                        } else if (this.items.get(i).isEmpty()) {
                            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
                            this.setItem(i, new ItemStack(Items.MELON_SLICE));
                            this.getItem(i).setCount(this.getItem(i).getCount() + rand.nextInt(6));
                            world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack) && (index == 0 || index == 1 || index == 2 || index == 3);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 4 || index == 5 ||index == 6 ||index == 7;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.steamworks.melon_farmer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new MelonFarmerContainer(id, playerInventory, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return 8;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty() || getItem(1).isEmpty() ||getItem(2).isEmpty() ||getItem(3).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        return ItemStackHelper.removeItem(items, index, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level != null && this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(8, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, this.items);
        this.progress = tags.getInt("Progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putInt("Progress", this.progress);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = this.getUpdateTag();
        ItemStackHelper.saveAllItems(tags, this.items);
        return new SUpdateTileEntityPacket(this.worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progress", this.progress);
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return this.handlers[0].cast();
            } else if (side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }

    }
}
