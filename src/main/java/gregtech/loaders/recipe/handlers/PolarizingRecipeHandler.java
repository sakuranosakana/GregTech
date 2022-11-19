package gregtech.loaders.recipe.handlers;

import gregtech.apiOld.GTValues;
import gregtech.apiOld.recipes.ModHandler;
import gregtech.apiOld.recipes.RecipeMaps;
import gregtech.apiOld.unification.OreDictUnifier;
import gregtech.apiOld.unification.material.Material;
import gregtech.apiOld.unification.material.properties.IngotProperty;
import gregtech.apiOld.unification.material.properties.PropertyKey;
import gregtech.apiOld.unification.ore.OrePrefix;
import gregtech.apiOld.unification.stack.UnificationEntry;
import net.minecraft.item.ItemStack;

import static gregtech.apiOld.GTValues.LV;
import static gregtech.apiOld.GTValues.VA;

public class PolarizingRecipeHandler {

    private static final OrePrefix[] POLARIZING_PREFIXES = new OrePrefix[]{
            OrePrefix.stick, OrePrefix.stickLong, OrePrefix.plate, OrePrefix.ingot, OrePrefix.plateDense, OrePrefix.rotor,
            OrePrefix.bolt, OrePrefix.screw, OrePrefix.wireFine, OrePrefix.foil, OrePrefix.ring};

    public static void register() {
        for (OrePrefix orePrefix : POLARIZING_PREFIXES) {
            orePrefix.addProcessingHandler(PropertyKey.INGOT, PolarizingRecipeHandler::processPolarizing);
        }
    }

    public static void processPolarizing(OrePrefix polarizingPrefix, Material material, IngotProperty property) {
        Material magneticMaterial = property.getMagneticMaterial();

        if (magneticMaterial != null && polarizingPrefix.doGenerateItem(magneticMaterial)) {
            ItemStack magneticStack = OreDictUnifier.get(polarizingPrefix, magneticMaterial);
            RecipeMaps.POLARIZER_RECIPES.recipeBuilder() //polarizing
                    .input(polarizingPrefix, material)
                    .outputs(magneticStack)
                    .duration((int) ((int) material.getMass() * polarizingPrefix.getMaterialAmount(material) / GTValues.M))
                    .EUt(8 * getVoltageMultiplier(material))
                    .buildAndRegister();

            ModHandler.addSmeltingRecipe(new UnificationEntry(polarizingPrefix, magneticMaterial),
                    OreDictUnifier.get(polarizingPrefix, material)); //de-magnetizing
        }
    }

    private static int getVoltageMultiplier(Material material) {
        return material.getBlastTemperature() >= 1200 ? VA[LV] : 2;
    }

}
