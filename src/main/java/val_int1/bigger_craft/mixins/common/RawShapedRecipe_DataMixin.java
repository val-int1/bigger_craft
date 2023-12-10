package val_int1.bigger_craft.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.recipe.RawShapedRecipe;

@Mixin(RawShapedRecipe.Data.class)
public class RawShapedRecipe_DataMixin {

	@ModifyConstant(method="method_55096", constant=@Constant(intValue=3))
	private static int biggerCraft_modConstMethod55096(int original) {
		return Integer.MAX_VALUE;
	}
	
}
