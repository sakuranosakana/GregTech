package gregtech.integration.jei.utils;

import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class MachineSubtypeHandler implements ISubtypeInterpreter {
    @Nonnull
    @Override
    public String apply(@Nonnull ItemStack itemStack) {
        return String.format("%d;", itemStack.getMetadata());
    }
}
