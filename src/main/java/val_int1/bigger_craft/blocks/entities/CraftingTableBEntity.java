package val_int1.bigger_craft.blocks.entities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import val_int1.bigger_craft.init.BCTInitCommon;

public class CraftingTableBEntity extends BlockEntity {

	private int gridWidth, gridHeight;
	private SimpleInventory inventory;
	
	public CraftingTableBEntity(BlockPos pos, BlockState state) {
		this(pos, state, 100, 100);
	}
	
	public CraftingTableBEntity(BlockPos pos, BlockState state, int gridWidth, int gridHeight) {
		super(BCTInitCommon.CHESTED_BE_TYPE, pos, state);
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.inventory = new SimpleInventory(this.gridWidth * this.gridHeight);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("gridWidth", this.gridWidth);
		nbt.putInt("gridHeight", this.gridHeight);
		Inventories.writeNbt(nbt, this.inventory.stacks);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.gridWidth = nbt.getInt("gridWidth");
		this.gridHeight = nbt.getInt("gridHeight");
		this.inventory = new SimpleInventory(this.gridWidth * this.gridHeight);
		Inventories.readNbt(nbt, this.inventory.stacks);
	}
	
	public ItemStack getItem(int i) {
		return this.inventory.getStack(i);
	}
	
	public void setItem(int i, ItemStack stack) {
		this.inventory.setStack(i, stack);
		this.markDirty();
	}

	public SimpleInventory getInventory() {
		return this.inventory;
	}

}
