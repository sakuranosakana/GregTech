package gregtech.apiOld.recipes.builders;

import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;
import gregtech.apiOld.recipes.ingredients.IntCircuitIngredient;
import gregtech.apiOld.util.EnumValidationResult;
import gregtech.apiOld.util.GTLog;
import gregtech.apiOld.util.GTUtility;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;

public class IntCircuitRecipeBuilder extends RecipeBuilder<IntCircuitRecipeBuilder> {

    protected int circuitMeta = -1;

    public IntCircuitRecipeBuilder() {
    }

    public IntCircuitRecipeBuilder(Recipe recipe, RecipeMap<IntCircuitRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public IntCircuitRecipeBuilder(RecipeBuilder<IntCircuitRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public boolean applyProperty(@Nonnull String key, Object value) {
        if (key.equals("circuit") && value instanceof Number) {
            circuitMeta(((Number) value).intValue());
            return true;
        }
        return super.applyProperty(key, value);
    }

    @Override
    public IntCircuitRecipeBuilder copy() {
        return new IntCircuitRecipeBuilder(this);
    }

    public IntCircuitRecipeBuilder circuitMeta(int circuitMeta) {
        if (!GTUtility.isBetweenInclusive(0, 32, circuitMeta)) {
            GTLog.logger.error("Integrated Circuit Metadata cannot be less than 0 and more than 32", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.circuitMeta = circuitMeta;
        return this;
    }

    @Override
    protected EnumValidationResult finalizeAndValidate() {
        if (circuitMeta >= 0) {
            inputs.add(IntCircuitIngredient.getOrCreate(new IntCircuitIngredient(circuitMeta)).setNonConsumable());
        }
        return super.finalizeAndValidate();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("circuitMeta", circuitMeta)
                .toString();
    }
}
