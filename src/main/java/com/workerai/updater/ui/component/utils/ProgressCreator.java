package com.workerai.updater.ui.component.utils;

import com.workerai.updater.ui.component.bar.ProgressBar;

import javax.swing.border.Border;
import java.awt.*;

import static com.workerai.updater.utils.ColorManager.DARK_YELLOW;
import static com.workerai.updater.utils.ColorManager.LIGHT_YELLOW;

public class ProgressCreator {
    public static ProgressBar createProgressBar(String name, int posX, int posY, int width, int height, int value, int maxValue) {
        ProgressBar progressBar = new ProgressBar(DARK_YELLOW, LIGHT_YELLOW);
        progressBar.setName(name);
        progressBar.setBounds(posX, posY, width, height);
        progressBar.setMaximum(maxValue);
        progressBar.setValue(value);
        progressBar.setBorder(new RoundBar(15));

        return progressBar;
    }

    public static class RoundBar implements Border {
        private final int r;

        public RoundBar(int r) {
            this.r = r;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.r + 1, this.r + 1, this.r + 2, this.r);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, r, r);
        }
    }
}
