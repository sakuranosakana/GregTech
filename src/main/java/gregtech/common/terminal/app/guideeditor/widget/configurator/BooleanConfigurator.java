package gregtech.common.terminal.app.guideeditor.widget.configurator;

import com.google.gson.JsonObject;
import gregtech.apiOld.gui.resources.ColorRectTexture;
import gregtech.apiOld.terminal.gui.widgets.DraggableScrollableWidgetGroup;
import gregtech.apiOld.terminal.gui.widgets.RectButtonWidget;
import gregtech.apiOld.terminal.os.TerminalTheme;

import java.awt.*;

public class BooleanConfigurator extends ConfiguratorWidget<Boolean>{

    public BooleanConfigurator(DraggableScrollableWidgetGroup group, JsonObject config, String name) {
        super(group, config, name);
    }

    public BooleanConfigurator(DraggableScrollableWidgetGroup group, JsonObject config, String name, boolean defaultValue) {
        super(group, config, name, defaultValue);
    }

    protected void init(){
        this.addWidget(new RectButtonWidget(0, 15, 10, 10, 2)
                .setToggleButton(new ColorRectTexture(new Color(198, 198, 198).getRGB()), (c, p)->updateValue(p))
                .setValueSupplier(true, ()->{
                    if(config.get(name).isJsonNull()) {
                        return defaultValue;
                    }
                    return config.get(name).getAsBoolean();
                })
                .setColors(TerminalTheme.COLOR_B_1.getColor(),
                        TerminalTheme.COLOR_1.getColor(),
                        TerminalTheme.COLOR_B_1.getColor())
                .setIcon(new ColorRectTexture(new Color(0, 0, 0, 74).getRGB())));
    }
}
