package gregtech.integration.jei.utils;

import gregtech.apiOld.items.metaitem.MetaItem;
import gregtech.apiOld.items.metaitem.MetaItem.MetaValueItem;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MetaItemSubtypeHandler implements ISubtypeInterpreter {
    @Nonnull
    @Override
    public String apply(ItemStack itemStack) {
        MetaItem<?> metaItem = (MetaItem<?>) itemStack.getItem();
        MetaValueItem metaValueItem = metaItem.getItem(itemStack);
        String additionalData = "";
        if (metaValueItem != null) {
            additionalData = metaValueItem.getSubItemHandler().getItemSubType(itemStack);
        }
        return String.format("%d;%s", itemStack.getMetadata(), additionalData);
    }
}
