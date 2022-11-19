package gregtech.common.terminal.component.setting;

import gregtech.apiOld.gui.Widget;
import gregtech.apiOld.gui.resources.IGuiTexture;
import gregtech.apiOld.gui.widgets.WidgetGroup;

import java.util.function.Consumer;

public class GuiTextureSetter extends WidgetGroup implements ISetting{
    private final String name;
    private Consumer<IGuiTexture> updated;

    public GuiTextureSetter(String name, Consumer<IGuiTexture> updated) {
        this.name = name;
        this.updated = updated;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IGuiTexture getIcon() {
        return null;
    }

    @Override
    public Widget getWidget() {
        return null;
    }
}
