package com.workerai.updater.ui.component.bar;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractProgressBar extends JComponent {
    private int value;

    private int maximum;

    private String string;

    public void setValue(int value) {
        this.value = value;

        repaint();
    }

    public int getValue() {
        return value;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;

        repaint();
    }

    public int getMaximum() {
        return maximum;
    }

    public void setString(String string) {
        if (string == null)
            throw new IllegalArgumentException("string == null");
        this.string = string;

        repaint();
    }

    public String getString() {
        return string;
    }
}
