package com.workerai.updater.ui.component.utils;

import com.workerai.updater.WorkerUpdater;
import com.workerai.updater.ui.component.button.AbstractButton;
import com.workerai.updater.ui.component.button.colored.ColoredButton;
import com.workerai.updater.ui.component.button.textured.TexturedButton;
import com.workerai.updater.utils.ResourceManager;

import javax.swing.border.Border;
import java.awt.*;

import static com.workerai.updater.ui.component.DrawComponent.getResource;
import static com.workerai.updater.utils.ColorManager.*;

public class ButtonCreator {
    public enum BUTTON_TYPE {
        LAUNCH_TEXTURED,
        CLOSE_TEXTURED,
        LAUNCH_COLORED,
        CLOSE_COLORED
    }

    public static AbstractButton createHoverButton(String name, int posX, int posY, int width, int height, BUTTON_TYPE type) {
        AbstractButton button;

        if (type.equals(BUTTON_TYPE.CLOSE_COLORED) || type.equals(BUTTON_TYPE.LAUNCH_COLORED)) {
            button = new ColoredButton(LIGHT_YELLOW, DARK_YELLOW);
        } else {
            if (type.equals(BUTTON_TYPE.LAUNCH_TEXTURED)) {
                button = new TexturedButton(getResource(ResourceManager.getPlayDarkIcon()), getResource(ResourceManager.getCloseLightIcon()));
            } else {
                button = new TexturedButton(getResource(ResourceManager.getCloseDarkIcon()), getResource(ResourceManager.getCloseLightIcon()));
            }
        }

        button.setText(name);
        button.setBounds(posX, posY, width, height);
        button.setTextColor(LIGHT_BLACK);
        button.setBorder(new RoundButton(15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addEventListener(e -> {
            if (type.equals(BUTTON_TYPE.CLOSE_TEXTURED) || type.equals(BUTTON_TYPE.CLOSE_COLORED)) {
                System.exit(0);
            } else if (type.equals(BUTTON_TYPE.LAUNCH_TEXTURED) || type.equals(BUTTON_TYPE.LAUNCH_COLORED)) {
                WorkerUpdater.getInstance().startWorkerLauncher();
            }
        });

        return button;
    }

    public static class RoundButton implements Border {
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
