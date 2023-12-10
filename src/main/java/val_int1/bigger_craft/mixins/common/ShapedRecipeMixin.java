package val_int1.bigger_craft.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.recipe.ShapedRecipe;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

	@ModifyConstant(method="getPattern", constant=@Constant(intValue=3))
	private static int biggerCraft_modConstGetPattern(int original) {
		return Integer.MAX_VALUE;
	}
	
	
}
