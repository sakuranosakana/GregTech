package gregtech.api.metatileentity.interfaces;


import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public interface IDataInfoProvider {

    @Nonnull
    List<ITextComponent> getDataInfo();
}
