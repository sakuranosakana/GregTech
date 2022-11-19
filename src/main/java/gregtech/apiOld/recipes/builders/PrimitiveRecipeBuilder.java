package gregtech.apiOld.recipes.builders;

import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;
import gregtech.apiOld.recipes.recipeproperties.PrimitiveProperty;
import gregtech.apiOld.util.ValidationResult;

public class PrimitiveRecipeBuilder extends RecipeBuilder<PrimitiveRecipeBuilder> {

    public PrimitiveRecipeBuilder() {
    }

    public PrimitiveRecipeBuilder(Recipe recipe, RecipeMap<PrimitiveRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public PrimitiveRecipeBuilder(RecipeBuilder<PrimitiveRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public PrimitiveRecipeBuilder copy() {
        return new PrimitiveRecipeBuilder(this);
    }

    @Override
    public ValidationResult<Recipe> build() {
        this.EUt(1); // secretly force to 1 to allow recipe matching to work properly
        applyProperty(PrimitiveProperty.getInstance(), true);
        return super.build();
    }
}
