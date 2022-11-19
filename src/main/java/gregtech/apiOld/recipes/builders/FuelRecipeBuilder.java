package gregtech.apiOld.recipes.builders;

import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;

public class FuelRecipeBuilder extends RecipeBuilder<FuelRecipeBuilder> {

    public FuelRecipeBuilder() {

    }

    public FuelRecipeBuilder(Recipe recipe, RecipeMap<FuelRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public FuelRecipeBuilder(RecipeBuilder<FuelRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public FuelRecipeBuilder copy() {
        return new FuelRecipeBuilder(this);
    }

    @Override
    public FuelRecipeBuilder EUt(int EUt) {
        return super.EUt(Math.abs(EUt) * -1);
    }

}
