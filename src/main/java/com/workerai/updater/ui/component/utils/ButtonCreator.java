package com.workerai.updater.ui.component.utils;

import com.workerai.updater.ui.component.button.AbstractButton;
import com.workerai.updater.ui.component.button.textured.TexturedButton;
import com.workerai.updater.utils.ResourceManager;

import javax.swing.border.Border;
import java.awt.*;

import static com.workerai.updater.ui.component.DrawComponent.getResource;
import static com.workerai.updater.utils.ColorManager.LIGHT_BLACK;

public class ButtonCreator {

    public static AbstractButton createHoverButton(String name, int posX, int posY, int width, int height) {
        AbstractButton button = new TexturedButton(getResource(ResourceManager.getCloseDarkIcon()), getResource(ResourceManager.getCloseLightIcon()));

        button.setText(name);
        button.setBounds(posX, posY, width, height);
        button.setTextColor(LIGHT_BLACK);
        button.setBorder(new RoundButton(15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addEventListener(e -> System.exit(0));

        return button;
    }

    static class RoundButton implements Border {
        private final int r;

        public RoundButton(int r) {
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
