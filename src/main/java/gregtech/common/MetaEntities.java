package gregtech.common;

import gregtech.GregTechMod;
import gregtech.api.GTValues;
import gregtech.client.renderer.handler.DynamiteRenderer;
import gregtech.client.renderer.handler.PortalRenderer;
import gregtech.client.renderer.handler.PowderBarrelRenderer;
import gregtech.common.entities.DynamiteEntity;
import gregtech.common.entities.PortalEntity;
import gregtech.common.entities.PowderbarrelEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MetaEntities {

    public static void init() {
        EntityRegistry.registerModEntity(new ResourceLocation(GTValues.MODID, "dynamite"), DynamiteEntity.class, "Dynamite", 1, GregTechMod.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(GTValues.MODID, "gtportal"), PortalEntity.class, "GTPortal", 2, GregTechMod.instance, 64, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(GTValues.MODID, "powderbarrel"), PowderbarrelEntity.class, "Powderbarrel", 3, GregTechMod.instance, 64, 3, true);
    }

    @SideOnly(Side.CLIENT)
    public static void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(DynamiteEntity.class, manager -> new DynamiteRenderer(manager, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(PortalEntity.class, manager -> new PortalRenderer(manager));
        RenderingRegistry.registerEntityRenderingHandler(PowderbarrelEntity.class, manager -> new PowderBarrelRenderer(manager));
    }
}
