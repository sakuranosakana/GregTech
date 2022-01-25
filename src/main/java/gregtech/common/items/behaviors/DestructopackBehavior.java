package gregtech.common.items.behaviors;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.items.gui.ItemUIFactory;
import gregtech.api.items.gui.PlayerInventoryHolder;
import gregtech.api.items.metaitem.stats.IItemBehaviour;
import gregtech.common.items.MetaItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class DestructopackBehavior implements IItemBehaviour, ItemUIFactory {

    private final IItemHandlerModifiable voidSlot;

    public DestructopackBehavior() {
        voidSlot = new DestructopackItemHandler();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote) {
            new PlayerInventoryHolder(player, hand).openUI();
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }

    @Override
    public ModularUI createUI(PlayerInventoryHolder holder, EntityPlayer entityPlayer) {
        ModularUI.Builder builder = ModularUI.defaultBuilder();
        builder.slot(voidSlot, 0, 79, 25, GuiTextures.SLOT, GuiTextures.DESTRUCTOPACK_OVERLAY); // todo position
        builder.bindPlayerInventory(holder.player.inventory);
        return builder.build(holder, entityPlayer);
    }

    private static class DestructopackItemHandler extends ItemStackHandler {

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        // Prevent destructopacks from being voided in themselves
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !MetaItems.DESTRUCTOPACK.isItemEqual(stack);
        }
    }
}
