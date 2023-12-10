package val_int1.bigger_craft.gui.handlers;

import static net.minecraft.screen.CraftingScreenHandler.updateResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import val_int1.bigger_craft.blocks.CraftingTableBlock;
import val_int1.bigger_craft.init.BCTInitCommon;

public class CraftingScreenHandler extends AbstractRecipeScreenHandler<RecipeInputInventory> {
	
	private final PlayerEntity player;
	private final World world;
	private final BlockPos pos;
	
	public final CraftingTableBlock table;
	public final int gridWidth, gridHeight, gridCount;
	
	private final RecipeInputInventory input;
	private final CraftingResultInventory result = new CraftingResultInventory();
	
	private boolean initialized = false; 
	
	public CraftingScreenHandler(int syncId, PlayerInventory playerInv, PacketByteBuf buf) {
		this(syncId, playerInv, playerInv.player.getWorld(), buf.readBlockPos());
	}
	
	public CraftingScreenHandler(int syncId, PlayerInventory playerInv, World world, BlockPos pos) {
		super(BCTInitCommon.TABLE_SCREEN_HANDLER, syncId);
		
		this.player = playerInv.player;
		this.world = world;
		this.pos = pos;
		
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(block instanceof CraftingTableBlock table) {
			this.table = table;
		} else {
			throw new IllegalArgumentException(block.toString() + " isn't a crafting table");
		}
		
		this.gridWidth = this.table.gridWidth;
		this.gridHeight = this.table.gridHeight;
		this.gridCount = (this.gridWidth * this.gridHeight) + 1; // + 1 because of output
		this.input = new CraftingInventory(this, this.gridWidth, this.gridHeight);
		
		int sizeExtraW = this.gridWidth - 3;
		int sizeExtraH = this.gridHeight - 3;
		this.addSlot(new CraftingResultSlot(this.player, this.input, this.result, 0, 124 + (18 * sizeExtraW), 35 + (9 * sizeExtraH)));
		
		int x, y;
		for(y = 0; y < this.gridHeight; y++) {
			for(x = 0; x < this.gridWidth; x++) {
				this.addSlot(new Slot(this.input, x + (y * this.gridWidth), 30 + (x * 18), 17 + (y * 18)));
			}
		}
		
		for(y = 0; y < 3; y++) {
			for(x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInv, 9 + x + (y * 9), 8 + (sizeExtraW * 9) + (x * 18), 84 + (sizeExtraH * 18) + (y * 18)));
			}
		}

		for(x = 0; x < 9; x++) {
			this.addSlot(new Slot(playerInv, x, 8 + (sizeExtraW * 9) + (x * 18), 142 + (sizeExtraH * 18)));
		}
		
		if(!world.isClient) {
			this.table.retrieveItems(world, pos, this.input);
		}
		
		this.initialized = true;
		updateResult(this, this.world, this.player, this.input, this.result);
	}
	
	@Override
	public void onContentChanged(Inventory inventory) {
		if(this.initialized) {
			updateResult(this, this.world, this.player, this.input, this.result);
		}
	}
	
	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		if(this.table.storeItems(this.world, this.pos, this.input)) {
			return;
		}
		this.dropInventory(player, this.input);
	}
	
	@Override
	public void populateRecipeFinder(RecipeMatcher finder) {
		this.input.provideRecipeInputs(finder);
	}

	@Override
	public void clearCraftingSlots() {
		this.input.clear();
		this.result.clear();
	}

	@Override
	public boolean matches(Recipe<? super RecipeInputInventory> recipe) {
		return recipe.matches(this.input, this.player.getWorld());
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
	}

	@Override
	public int getCraftingWidth() {
		return this.gridWidth;
	}

	@Override
	public int getCraftingHeight() {
		return this.gridHeight;
	}

	@Override
	public int getCraftingSlotCount() {
		return this.gridCount;
	}

	@Override
	public RecipeBookCategory getCategory() {
		return RecipeBookCategory.CRAFTING;
	}

	@Override
	public boolean canInsertIntoSlot(int index) {
		return index != 0;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = this.slots.get(slot);
		if(slot2 != null && slot2.hasStack()) {
			ItemStack itemStack2 = slot2.getStack();
			itemStack = itemStack2.copy();
			if(slot == 0) {
				itemStack2.getItem().onCraft(itemStack2, this.world, player);
				if(!this.insertItem(itemStack2, this.gridCount, this.gridCount + 36, true)) {
					return ItemStack.EMPTY;
				}

				slot2.onQuickTransfer(itemStack2, itemStack);
			} else if(slot >= this.gridCount && slot < this.gridCount + 36) {
				if(!this.insertItem(itemStack2, 1, this.gridCount, false)) {
					if(slot < this.gridCount + 27) {
						if(!this.insertItem(itemStack2, this.gridCount + 27, this.gridCount + 36, false)) {
							return ItemStack.EMPTY;
						}
					} else if(!this.insertItem(itemStack2, this.gridCount, this.gridCount + 27, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if(!this.insertItem(itemStack2, this.gridCount, this.gridCount + 36, false)) {
				return ItemStack.EMPTY;
			}

			if(itemStack2.isEmpty()) {
				slot2.setStack(ItemStack.EMPTY);
			} else {
				slot2.markDirty();
			}

			if(itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot2.onTakeItem(player, itemStack2);
			if(slot == 0) {
				player.dropItem(itemStack2, false);
			}
		}

		return itemStack;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.world.getBlockState(this.pos).isOf(this.table) && player.squaredDistanceTo(Vec3d.ofCenter(this.pos)) <= 64.0;
	}
	
}

