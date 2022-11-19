package gregtech.apiOld.terminal.os.menu;

import gregtech.apiOld.gui.Widget;
import gregtech.apiOld.gui.resources.ColorRectTexture;
import gregtech.apiOld.gui.resources.IGuiTexture;

public interface IMenuComponent {
    /**
     * Component Icon
     */
    default IGuiTexture buttonIcon() {
        return new ColorRectTexture(0);
    }

    /**
     * Component Hover Text
     */
    default String hoverText() {
        return null;
    }

    /**
     * Click Event. Side see {@link Widget.ClickData#isClient}
     */
    default void click(Widget.ClickData clickData){}
}
