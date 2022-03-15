package gregtech.common.terminal.app.guide;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gregtech.api.GTValues;
import gregtech.api.GregTechAPI;
import gregtech.api.gui.resources.IGuiTexture;
import gregtech.api.gui.resources.ItemStackTexture;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.util.ResourceLocation;

public class MultiBlockGuideApp extends GuideApp<IMetaTileEntity> {

    public MultiBlockGuideApp() {
        super("multiblocks", new ItemStackTexture(MetaTileEntities.ELECTRIC_BLAST_FURNACE.getStackForm()));
    }

    @Override
    public IMetaTileEntity ofJson(JsonObject json) {
        String[] valids = {"multiblock", "metatileentity"};
        if (json.isJsonObject()) {
            for (String valid : valids) {
                JsonElement id = json.getAsJsonObject().get(valid);
                if (id != null && id.isJsonPrimitive())
                    return GregTechAPI.MTE_REGISTRY.getObject(new ResourceLocation(GTValues.MODID, id.getAsString()));
            }
        }
        return null;
    }

    @Override
    protected IGuiTexture itemIcon(IMetaTileEntity item) {
        return new ItemStackTexture(item.getStackForm());
    }

    @Override
    protected String itemName(IMetaTileEntity item) {
        return item.getStackForm().getDisplayName();
    }

    @Override
    protected String rawItemName(IMetaTileEntity item) {
        return item.getMetaTileEntityId().getPath();
    }
}
