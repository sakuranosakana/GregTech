package gregtech.apiOld.gui.widgets;

import gregtech.apiOld.gui.Widget;
import gregtech.apiOld.util.Position;
import gregtech.apiOld.util.Size;

public class WidgetGroup extends AbstractWidgetGroup {

    public WidgetGroup() {
        this(Position.ORIGIN);
    }

    public WidgetGroup(Position position) {
        super(position);
    }

    public WidgetGroup(Position position, Size size) {
        super(position, size);
    }

    public WidgetGroup(int x, int y, int width, int height) {
        super(new Position(x, y), new Size(width, height));
    }

    @Override
    public void addWidget(Widget widget) {
        super.addWidget(widget);
    }

    @Override
    public void removeWidget(Widget widget) {
        super.removeWidget(widget);
    }

    @Override
    public void waitToRemoved(Widget widget) {
        super.waitToRemoved(widget);
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
    }
}
