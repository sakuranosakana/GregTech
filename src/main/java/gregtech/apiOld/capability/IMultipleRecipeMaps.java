package gregtech.apiOld.capability;

import gregtech.apiOld.recipes.RecipeMap;

// TODO IRecipeMap
public interface IMultipleRecipeMaps {

    /**
     * Used to get all possible RecipeMaps a Multiblock can run
     * @return array of RecipeMaps
     */
    RecipeMap<?>[] getAvailableRecipeMaps();

    /**
     *
     * @return the currently selected RecipeMap
     */
    RecipeMap<?> getCurrentRecipeMap();
}
