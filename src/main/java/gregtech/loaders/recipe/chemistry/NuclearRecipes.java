package gregtech.loaders.recipe.chemistry;

import static gregtech.apiOld.GTValues.*;
import static gregtech.apiOld.recipes.RecipeMaps.*;
import static gregtech.material.Materials.*;
import static gregtech.apiOld.unification.ore.OrePrefix.dust;

public class NuclearRecipes {

    public static void init() {

        CHEMICAL_RECIPES.recipeBuilder().duration(200).EUt(VA[LV])
                .input(dust, Uraninite, 3)
                .fluidInputs(HydrofluoricAcid.getFluid(4000))
                .fluidInputs(Fluorine.getFluid(2000))
                .fluidOutputs(UraniumHexafluoride.getFluid(1000))
                .fluidOutputs(Water.getFluid(2000))
                .buildAndRegister();

        CENTRIFUGE_RECIPES.recipeBuilder().duration(160).EUt(VA[HV])
                .fluidInputs(UraniumHexafluoride.getFluid(1000))
                .fluidOutputs(EnrichedUraniumHexafluoride.getFluid(100))
                .fluidOutputs(DepletedUraniumHexafluoride.getFluid(900))
                .buildAndRegister();

        ELECTROLYZER_RECIPES.recipeBuilder().duration(160).EUt(VA[MV])
                .fluidInputs(EnrichedUraniumHexafluoride.getFluid(1000))
                .output(dust, Uranium235)
                .fluidOutputs(Fluorine.getFluid(6000))
                .buildAndRegister();

        ELECTROLYZER_RECIPES.recipeBuilder().duration(160).EUt(VA[MV])
                .fluidInputs(DepletedUraniumHexafluoride.getFluid(1000))
                .output(dust, Uranium238)
                .fluidOutputs(Fluorine.getFluid(6000))
                .buildAndRegister();

    }
}
