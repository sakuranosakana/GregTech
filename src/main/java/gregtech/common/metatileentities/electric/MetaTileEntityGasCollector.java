package gregtech.common.metatileentities.electric;

import gregtech.apiOld.capability.IEnergyContainer;
import gregtech.apiOld.capability.impl.RecipeLogicEnergy;
import gregtech.apiOld.metatileentity.MetaTileEntity;
import gregtech.apiOld.metatileentity.SimpleMachineMetaTileEntity;
import gregtech.apiOld.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.apiOld.recipes.Recipe;
import gregtech.apiOld.recipes.RecipeMap;
import gregtech.apiOld.recipes.RecipeMaps;
import gregtech.apiOld.recipes.recipeproperties.GasCollectorDimensionProperty;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import it.unimi.dsi.fastutil.ints.IntLists;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.function.Function;
import java.util.function.Supplier;

public class MetaTileEntityGasCollector extends SimpleMachineMetaTileEntity {

    public MetaTileEntityGasCollector(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap, ICubeRenderer renderer, int tier, boolean hasFrontFacing,
                                      Function<Integer, Integer> tankScalingFunction) {
        super(metaTileEntityId, recipeMap, renderer, tier, hasFrontFacing, tankScalingFunction);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityGasCollector(this.metaTileEntityId, RecipeMaps.GAS_COLLECTOR_RECIPES,
                Textures.GAS_COLLECTOR_OVERLAY, this.getTier(), this.hasFrontFacing(), this.getTankScalingFunction());
    }

    @Override
    protected RecipeLogicEnergy createWorkable(RecipeMap<?> recipeMap) {
        return new GasCollectorRecipeLogic(this, recipeMap, () -> energyContainer);
    }

    protected boolean checkRecipe(@Nonnull Recipe recipe) {
        for (int dimension : recipe.getProperty(GasCollectorDimensionProperty.getInstance(), IntLists.EMPTY_LIST)) {
            if (dimension == this.getWorld().provider.getDimension()) {
                return true;
            }
        }
        return false;
    }

    private static class GasCollectorRecipeLogic extends RecipeLogicEnergy {

        public GasCollectorRecipeLogic(MetaTileEntity metaTileEntity, RecipeMap<?> recipeMap, Supplier<IEnergyContainer> energyContainer) {
            super(metaTileEntity, recipeMap, energyContainer);
        }

        @Override
        protected boolean checkRecipe(@Nonnull Recipe recipe) {
            return ((MetaTileEntityGasCollector) metaTileEntity).checkRecipe(recipe) && super.checkRecipe(recipe);
        }
    }
}
