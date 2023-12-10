package val_int1.bigger_craft.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import val_int1.bigger_craft.blocks.entities.CraftingTableBEntity;
import val_int1.bigger_craft.gui.handlers.CraftingScreenHandler;

public abstract class CraftingTableBlock extends Block {

	public final Identifier tableId; // Not necessarily matching the block's registry ID
	public final int gridWidth, gridHeight;
	
	private CraftingTableBlock(Identifier tableId, int gridWidth, int gridHeight) {
		super(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE));
		this.tableId = tableId;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
	}
	
	public final static CraftingTableBlock create(Identifier tableId, int gridWidth, int gridHeight, boolean holdsItems) {
		return holdsItems ? new Chested(tableId, gridWidth, gridHeight) : new Dropping(tableId, gridWidth, gridHeight); 
	}
	
	@Override
	public final ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
			// TODO: maybe create a stat for each block
			//player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	public final NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		Text title = Text.translatable("container.crafting." + this.tableId.getNamespace() + "." + this.tableId.getPath());
		return new ExtendedScreenHandlerFactory() {
			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new CraftingScreenHandler(syncId, playerInventory, world, pos);
			}
			@Override
			public Text getDisplayName() {
				return title;
			}
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
				buf.writeBlockPos(pos);
			}
		};
	}
	
	public abstract boolean storeItems(World world, BlockPos pos, RecipeInputInventory input);
	public abstract void retrieveItems(World world, BlockPos pos, RecipeInputInventory input);
	
	private final static class Dropping extends CraftingTableBlock {
		
		public Dropping(Identifier tableId, int gridWidth, int gridHeight) {
			super(tableId, gridWidth, gridHeight);
		}
		
		@Override
		public boolean storeItems(World world, BlockPos pos, RecipeInputInventory input) {
			return false;
		}
		
		@Override
		public void retrieveItems(World world, BlockPos pos, RecipeInputInventory input) {
			// NOOP
		}
		
	}
	
	private final static class Chested extends CraftingTableBlock implements BlockEntityProvider {

		private Chested(Identifier tableId, int gridWidth, int gridHeight) {
			super(tableId, gridWidth, gridHeight);
		}
		
		@Override
		protected void appendProperties(Builder<Block, BlockState> builder) {
			builder.add(Properties.HORIZONTAL_FACING);
		}

		@Override
		public BlockState getPlacementState(ItemPlacementContext ctx) {
			return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
		}
		
		@Override
		public boolean storeItems(World world, BlockPos pos, RecipeInputInventory input) {
			BlockEntity bentity = world.getBlockEntity(pos);
			if(bentity == null || !(bentity instanceof CraftingTableBEntity table)) {
				return false;
			}
			for(int i = 0; i < input.size(); i++) {
				table.setItem(i, input.getStack(i));
			}
			return true;
		}
		
		@Override
		public void retrieveItems(World world, BlockPos pos, RecipeInputInventory input) {
			BlockEntity bentity = world.getBlockEntity(pos);
			if(bentity == null || !(bentity instanceof CraftingTableBEntity table)) {
				return;
			}
			for(int i = 0; i < input.size(); i++) {
				input.setStack(i, table.getItem(i));
			}
		}

		@Override
		public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new CraftingTableBEntity(pos, state, this.gridWidth, this.gridHeight);
		}
		
		@Override
		@SuppressWarnings("deprecation") // more like @SuprpressWarnings("mojang_misusing_java_language_features")
		public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
			if(!newState.isOf(this)) {
				BlockEntity bentity = world.getBlockEntity(pos);
				if(bentity != null && bentity instanceof CraftingTableBEntity table) {
					ItemScatterer.spawn(world, pos, table.getInventory());
					world.updateComparators(pos, this);
				}
			}
			
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
}

