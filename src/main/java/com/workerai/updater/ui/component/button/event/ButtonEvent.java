package com.workerai.updater.ui.component.button.event;

public class ButtonEvent {
    public static final int BUTTON_CLICKED_EVENT = 0;

    private final Object source;

    private int type;

    public ButtonEvent(Object source, int type) {
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }

    public int getType() {
        return this.type;
    }
}
