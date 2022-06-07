package com.wilyfox.steamworks.blocks.TreeFarmer;

import com.wilyfox.steamworks.setup.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
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

public class TreeFarmerTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    static final int WORK_TIME = 2 * 20;
    private static final int FREQUENCY_TICKS = 20;

    int acaciaLogsCount = 0;
    int birchLogsCount = 0;
    int darkOakLogsCount = 0;
    int jungleLogsCount = 0;
    int oakLogsCount = 0;
    int spruceLogsCount = 0;

    int acaciaSaplingsCount = 0;
    int birchSaplingsCount = 0;
    int darkOakSaplingsCount = 0;
    int jungleSaplingsCount = 0;
    int oakSaplingsCount = 0;
    int spruceSaplingsCount = 0;

    int applesCount = 0;

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

    public TreeFarmerTileEntity() {
        super(ModTileEntityTypes.TREE_FARMER.get());
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(8, ItemStack.EMPTY);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    private void recursiveScanLeaves(BlockPos pos) {
        World world = this.level;
        assert world != null;
        Random rand = new Random();

        for (int x = -1; x <= 1 ; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y <= 10; y++) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (checkIfLeavBlock(newPos)) {
                        int chance = rand.nextInt(100);
                        int appleChance = rand.nextInt(200);
                        if (world.getBlockState(newPos).getBlock() == Blocks.ACACIA_LEAVES) {
                            if (chance <= 5) {
                                acaciaSaplingsCount++;
                            }
                        } else if (world.getBlockState(newPos).getBlock() == Blocks.BIRCH_LEAVES) {
                            if (chance <= 5) {
                                birchSaplingsCount++;
                            }
                        } else if (world.getBlockState(newPos).getBlock() == Blocks.DARK_OAK_LEAVES) {
                            if (chance <= 5) {
                                darkOakSaplingsCount++;
                            }
                            if (appleChance == 1) {
                                applesCount++;
                            }
                        } else if (world.getBlockState(newPos).getBlock() == Blocks.JUNGLE_LEAVES) {
                            if (chance <= 5) {
                                jungleSaplingsCount++;
                            }
                        } else if (world.getBlockState(newPos).getBlock() == Blocks.OAK_LEAVES) {
                            if (chance <= 5) {
                                oakSaplingsCount++;
                            }
                            if (appleChance == 1) {
                                applesCount++;
                            }
                        } else if (world.getBlockState(newPos).getBlock() == Blocks.SPRUCE_LEAVES) {
                            if (chance <= 5) {
                                spruceSaplingsCount++;
                            }
                        }
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanLeaves(newPos);
                    }
                }
            }
        }
    }

    private void recursiveScanTree(BlockPos pos) {
        World world = this.level;
        assert world != null;

        for (int x = -2; x <= 2 ; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 1; y++) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    if (world.getBlockState(newPos).getBlock() == Blocks.ACACIA_LOG) {
                        acaciaLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (world.getBlockState(newPos).getBlock() == Blocks.BIRCH_LOG) {
                        birchLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (world.getBlockState(newPos).getBlock() == Blocks.DARK_OAK_LOG) {
                        darkOakLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (world.getBlockState(newPos).getBlock() == Blocks.JUNGLE_LOG) {
                        jungleLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (world.getBlockState(newPos).getBlock() == Blocks.OAK_LOG) {
                        oakLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (world.getBlockState(newPos).getBlock() == Blocks.SPRUCE_LOG) {
                        spruceLogsCount++;
                        world.setBlockAndUpdate(newPos, Blocks.AIR.defaultBlockState());
                        recursiveScanTree(newPos);
                    } else if (checkIfLeavBlock(newPos)) {
                        recursiveScanLeaves(newPos);
                    }
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
                }
            }
        }
    }

    private boolean checkIfTreeBlock(BlockPos pos) {
        World world = this.level;
        assert world != null;
        return world.getBlockState(pos).getBlock() == Blocks.ACACIA_LOG ||
                world.getBlockState(pos).getBlock() == Blocks.BIRCH_LOG ||
                world.getBlockState(pos).getBlock() == Blocks.DARK_OAK_LOG ||
                world.getBlockState(pos).getBlock() == Blocks.JUNGLE_LOG ||
                world.getBlockState(pos).getBlock() == Blocks.OAK_LOG ||
                world.getBlockState(pos).getBlock() == Blocks.SPRUCE_LOG;
    }

    private boolean checkIfLeavBlock(BlockPos pos) {
        World world = this.level;
        assert world != null;
        return world.getBlockState(pos).getBlock() == Blocks.ACACIA_LEAVES ||
                world.getBlockState(pos).getBlock() == Blocks.BIRCH_LEAVES ||
                world.getBlockState(pos).getBlock() == Blocks.DARK_OAK_LEAVES ||
                world.getBlockState(pos).getBlock() == Blocks.JUNGLE_LEAVES ||
                world.getBlockState(pos).getBlock() == Blocks.OAK_LEAVES ||
                world.getBlockState(pos).getBlock() == Blocks.SPRUCE_LEAVES;
    }

    private void insertLogs(Item logType, int logsCount) {
        for (int i = 4; i < 8; i++) {
            if (this.items.get(i).getItem() == logType && this.items.get(i).getCount() < 64) {
                this.items.get(i).setCount(this.items.get(i).getCount() + logsCount);
                if (this.items.get(i).getCount() > 64) {
                    int delta = this.items.get(i).getCount() - 64;
                    this.items.get(i).setCount(64);
                    insertLogs(logType, delta);
                }
                return;
            } else if (this.items.get(i).isEmpty()) {
                this.setItem(i, new ItemStack(logType));
                this.items.get(i).setCount(this.items.get(i).getCount() + logsCount - 1);
                if (this.items.get(i).getCount() > 64) {
                    int delta = this.items.get(i).getCount() - 64;
                    this.items.get(i).setCount(64);
                    insertLogs(logType, delta);
                }
                return;
            }
        }
    }

    private void insertSaplings(Item saplingType, int saplingsCount) {
        for (int i = 0; i < 8; i++) {
            if (this.items.get(i).getItem() == saplingType && this.items.get(i).getCount() < 64) {
                this.items.get(i).setCount(this.items.get(i).getCount() + saplingsCount);
                if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
                return;
            } else if (this.items.get(i).isEmpty()) {
                this.setItem(i, new ItemStack(saplingType));
                this.items.get(i).setCount(this.items.get(i).getCount() + saplingsCount - 1);
                if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
                return;
            }
        }
    }

    @Override
    public void tick() {
        World world = this.level;
        assert world != null;
        Random rand = new Random();

        for (int x = -2; x <= 2 ; x++) {
            for (int y = -2; y <= 2 ; y++) {
                BlockPos pos = this.worldPosition;
                BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + y);

                if (world.getBlockState(newPos) == Blocks.AIR.defaultBlockState() &&
                        (world.getBlockState(newPos.below()).getBlock() == Blocks.GRASS_BLOCK
                        || world.getBlockState(newPos.below()).getBlock() == Blocks.DIRT
                        || world.getBlockState(newPos.below()).getBlock() == Blocks.COARSE_DIRT
                        || world.getBlockState(newPos.below()).getBlock() == Blocks.PODZOL)) {
                    for (int i = 0; i < 4; i++) {
                        if (this.items.get(i).getItem() == Items.ACACIA_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.ACACIA_SAPLING.defaultBlockState());
                            break;
                        } else if (this.items.get(i).getItem() == Items.BIRCH_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.BIRCH_SAPLING.defaultBlockState());
                            break;
                        } else if (this.items.get(i).getItem() == Items.DARK_OAK_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.DARK_OAK_SAPLING.defaultBlockState());
                            break;
                        } else if (this.items.get(i).getItem() == Items.JUNGLE_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.JUNGLE_SAPLING.defaultBlockState());
                            break;
                        } else if (this.items.get(i).getItem() == Items.OAK_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.OAK_SAPLING.defaultBlockState());
                            break;
                        } else if (this.items.get(i).getItem() == Items.SPRUCE_SAPLING) {
                            this.items.get(i).setCount(this.items.get(i).getCount() - 1);
                            world.setBlockAndUpdate(newPos, Blocks.SPRUCE_SAPLING.defaultBlockState());
                            break;
                        }
                    }
                } else {
                    if (this.items.get(4).getCount() >= 64 && this.items.get(5).getCount() >= 64 && this.items.get(6).getCount() >= 64 && this.items.get(7).getCount() >= 64) return;
                    if (checkIfTreeBlock(newPos)) {
                        if (this.items.get(4).getCount() >= 64 && this.items.get(5).getCount() >= 64 && this.items.get(6).getCount() >= 64 && this.items.get(7).getCount() >= 64) return;
                        recursiveScanTree(pos);
                        insertLogs(Items.ACACIA_LOG, acaciaLogsCount);
                        insertLogs(Items.BIRCH_LOG, birchLogsCount);
                        insertLogs(Items.DARK_OAK_LOG, darkOakLogsCount);
                        insertLogs(Items.JUNGLE_LOG, jungleLogsCount);
                        insertLogs(Items.OAK_LOG, oakLogsCount);
                        insertLogs(Items.SPRUCE_LOG, spruceLogsCount);

                        insertSaplings(Items.ACACIA_SAPLING, acaciaSaplingsCount);
                        insertSaplings(Items.BIRCH_SAPLING, birchSaplingsCount);
                        insertSaplings(Items.DARK_OAK_SAPLING, darkOakSaplingsCount);
                        insertSaplings(Items.JUNGLE_SAPLING, jungleSaplingsCount);
                        insertSaplings(Items.OAK_SAPLING, oakSaplingsCount);
                        insertSaplings(Items.SPRUCE_SAPLING, spruceSaplingsCount);

                        insertLogs(Items.APPLE, applesCount);

                        acaciaSaplingsCount = 0;
                        birchSaplingsCount = 0;
                        darkOakSaplingsCount = 0;
                        jungleSaplingsCount = 0;
                        oakSaplingsCount = 0;
                        spruceSaplingsCount = 0;

                        acaciaLogsCount = 0;
                        birchLogsCount = 0;
                        darkOakLogsCount = 0;
                        jungleLogsCount = 0;
                        oakLogsCount = 0;
                        spruceLogsCount = 0;

                        applesCount = 0;
//                        for (int i = 4; i < 8; i++) {
//                            if (this.items.get(i).getItem() == Items.APPLE && this.items.get(i).getCount() < 64) {
//                                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
//                                this.items.get(i).setCount(this.items.get(i).getCount() + applesCount);
//                                if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
//                                applesCount = 0;
//                                if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
//                                break;
//                            } else if (this.items.get(i).isEmpty()) {
//                                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
//                                recursiveScanTree(pos);
//                                this.setItem(i, new ItemStack(Items.APPLE));
//                                this.items.get(i).setCount(this.items.get(i).getCount() + applesCount - 1);
//                                applesCount = 0;
//                            }
//                        }
//
//                        for (int i = 0; i < 4; i++) {
//                            if (this.items.get(i).getItem() == Items.OAK_SAPLING && this.items.get(i).getCount() < 64) {
//                                this.items.get(i).setCount(this.items.get(i).getCount() + oakSaplingsCount);
//                                oakSaplingsCount = 0;
//                                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
//                                break;
//                            } else if (this.items.get(i).isEmpty()) {
//                                this.setItem(i, new ItemStack(Items.OAK_SAPLING));
//                                this.items.get(i).setCount(this.items.get(i).getCount() + oakSaplingsCount - 1);
//                                if (this.items.get(i).getCount() > 64) this.items.get(i).setCount(64);
//                                oakSaplingsCount = 0;
//                                this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, newPos.getX(), newPos.getY(), newPos.getZ(), 1.0, 1.0, 1.0);
//                                break;
//                            }
//                        }
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
        return this.canPlaceItem(index, stack) && (index == 0 || index == 1 ||index == 2 ||index == 3);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 4 || index == 5 ||index == 6 ||index == 7;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.steamworks.tree_farmer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new TreeFarmerContainer(id, playerInventory, this, this.fields);
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
