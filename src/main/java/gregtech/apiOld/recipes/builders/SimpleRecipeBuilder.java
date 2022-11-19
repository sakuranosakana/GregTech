package gregtech.apiOld.recipes.builders;

import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;

public class SimpleRecipeBuilder extends RecipeBuilder<SimpleRecipeBuilder> {

    public SimpleRecipeBuilder() {
    }

    public SimpleRecipeBuilder(Recipe recipe, RecipeMap<SimpleRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public SimpleRecipeBuilder(RecipeBuilder<SimpleRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public SimpleRecipeBuilder copy() {
        return new SimpleRecipeBuilder(this);
    }

}
