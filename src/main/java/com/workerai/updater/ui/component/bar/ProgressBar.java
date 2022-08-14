package com.workerai.updater.ui.component.bar;

import java.awt.*;

import static com.workerai.updater.ui.component.DrawComponent.fillFullsizedRect;
import static com.workerai.updater.ui.component.DrawComponent.*;

public class ProgressBar extends AbstractProgressBar {
    private Color background;
    private Color foreground;


    public ProgressBar(Color background, Color foreground) {
        if (background == null)
            throw new IllegalArgumentException("background == null");
        this.background = background;

        if (foreground == null)
            this.foreground = background.brighter();
        else
            this.foreground = foreground;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        fillFullsizedRect(g, this, background);

        int fgSize = crossMult(getValue(), getMaximum(), this.getWidth());

        if (fgSize > 0) {
            g.setColor(foreground);
            g.fillRoundRect(0, 0, fgSize, this.getHeight(), 15, 15);
        }
    }

    public void setBackground(Color background) {
        if (background == null)
            throw new IllegalArgumentException("background == null");
        this.background = background;

        repaint();
    }

    public Color getBackground() {
        return background;
    }

    public void setForeground(Color foreground) {
        if (foreground == null)
            throw new IllegalArgumentException("foreground == null");
        this.foreground = foreground;

        repaint();
    }

    public Color getForeground() {
        return foreground;
    }
}
