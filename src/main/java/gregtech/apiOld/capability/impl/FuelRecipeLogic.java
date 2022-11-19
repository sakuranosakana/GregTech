package gregtech.apiOld.capability.impl;

import gregtech.apiOld.capability.IEnergyContainer;
import gregtech.apiOld.metatileentity.MetaTileEntity;
import gregtech.apiOld.metatileentity.multiblock.ParallelLogicType;
import gregtech.apiOld.recipes.RecipeBuilder;
import gregtech.apiOld.recipes.RecipeMap;
import gregtech.apiOld.recipes.recipeproperties.IRecipePropertyStorage;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class FuelRecipeLogic extends RecipeLogicEnergy {

    public FuelRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
        super(tileEntity, recipeMap, energyContainer);
    }

    @Override
    protected int[] runOverclockingLogic(IRecipePropertyStorage propertyStorage, int recipeEUt, long maxVoltage, int recipeDuration, int amountOC) {
        // no overclocking happens other than parallelization,
        // so return the recipe's values, with EUt made positive for it to be made negative later
        return new int[]{recipeEUt * -1, recipeDuration};
    }

    @Override
    public Enum<ParallelLogicType> getParallelLogicType() {
        return ParallelLogicType.MULTIPLY; //TODO APPEND_FLUIDS
    }

    @Override
    protected boolean hasEnoughPower(@Nonnull int[] resultOverclock) {
        // generators always have enough power to run recipes
        return true;
    }

    @Override
    public void applyParallelBonus(@Nonnull RecipeBuilder<?> builder) {
        // the builder automatically multiplies by -1, so nothing extra is needed here
        builder.EUt(builder.getEUt());
    }

    @Override
    public int getParallelLimit() {
        // parallel is limited by voltage
        return Integer.MAX_VALUE;
    }
}
