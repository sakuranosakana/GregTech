package gregtech.apiOld.capability.impl;

import gregtech.api.capability.IHeatingCoil;
import gregtech.apiOld.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.apiOld.recipes.recipeproperties.IRecipePropertyStorage;
import gregtech.apiOld.recipes.recipeproperties.TemperatureProperty;
import net.minecraft.util.Tuple;

import javax.annotation.Nonnull;

import static gregtech.apiOld.recipes.logic.OverclockingLogic.heatingCoilOverclockingLogic;

/**
 * RecipeLogic for multiblocks that use temperature for raising speed and lowering energy usage
 * Used with RecipeMaps that run recipes using the {@link TemperatureProperty}
 */
public class HeatingCoilRecipeLogic extends MultiblockRecipeLogic {

    public HeatingCoilRecipeLogic(RecipeMapMultiblockController metaTileEntity) {
        super(metaTileEntity);
    }

    @Override
    protected int[] runOverclockingLogic(@Nonnull IRecipePropertyStorage propertyStorage, int recipeEUt, long maxVoltage, int duration, int amountOC) {
        // apply maintenance penalties
        Tuple<Integer, Double> maintenanceValues = getMaintenanceValues();

        return heatingCoilOverclockingLogic(
                Math.abs(recipeEUt),
                maxVoltage,
                (int) Math.round(duration * maintenanceValues.getSecond()),
                amountOC,
                ((IHeatingCoil) metaTileEntity).getCurrentTemperature(),
                propertyStorage.getRecipePropertyValue(TemperatureProperty.getInstance(), 0)
        );
    }


}
