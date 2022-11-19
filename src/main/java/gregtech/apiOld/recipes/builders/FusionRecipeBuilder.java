package gregtech.apiOld.recipes.builders;

import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;
import gregtech.apiOld.recipes.recipeproperties.FusionEUToStartProperty;
import gregtech.apiOld.util.EnumValidationResult;
import gregtech.apiOld.util.GTLog;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;

public class FusionRecipeBuilder extends RecipeBuilder<FusionRecipeBuilder> {

    public FusionRecipeBuilder() {
    }

    public FusionRecipeBuilder(Recipe recipe, RecipeMap<FusionRecipeBuilder> recipeMap) {
        super(recipe, recipeMap);
    }

    public FusionRecipeBuilder(RecipeBuilder<FusionRecipeBuilder> recipeBuilder) {
        super(recipeBuilder);
    }

    @Override
    public FusionRecipeBuilder copy() {
        return new FusionRecipeBuilder(this);
    }

    @Override
    public boolean applyProperty(@Nonnull String key, Object value) {
        if (key.equals(FusionEUToStartProperty.KEY)) {
            this.EUToStart(((Number) value).longValue());
            return true;
        }
        return super.applyProperty(key, value);
    }

    public FusionRecipeBuilder EUToStart(long EUToStart) {
        if (EUToStart <= 0) {
            GTLog.logger.error("EU to start cannot be less than or equal to 0", new IllegalArgumentException());
            recipeStatus = EnumValidationResult.INVALID;
        }
        this.applyProperty(FusionEUToStartProperty.getInstance(), EUToStart);
        return this;
    }

    public long getEUToStart() {
        return this.recipePropertyStorage == null ? 0L :
                this.recipePropertyStorage.getRecipePropertyValue(FusionEUToStartProperty.getInstance(), 0L);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append(FusionEUToStartProperty.getInstance().getKey(), getEUToStart())
                .toString();
    }
}
