package gregtech.common.terminal.component.setting;

import gregtech.apiOld.gui.Widget;
import gregtech.apiOld.gui.resources.IGuiTexture;

public interface ISetting {
    String getName();
    IGuiTexture getIcon();
    Widget getWidget();
}
