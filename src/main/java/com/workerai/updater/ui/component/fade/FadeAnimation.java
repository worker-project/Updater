package com.workerai.updater.ui.component.fade;

import com.sun.awt.AWTUtilities;
import java.awt.Window;

public class FadeAnimation {
    public static final int FAST = 5;

    public static void query(final long to, final long toWait, final LoopAction loopAction) {
        Thread t = new Thread(() -> {
            for (long query = 0; query < to + 1; query += 1) {
                loopAction.onLoop(query);
                try {
                    Thread.sleep(toWait);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
    }

    public static void fadeInFrame(Window toFade, int speed) {
        fade(toFade, speed, false);
    }

    public static void fadeOutFrame(Window toFade, int speed) {
        fade(toFade, speed, true);
    }

    private static void fade(final Window toFade, final int speed, final boolean inverted) {
        query(100L, speed, query -> AWTUtilities.setWindowOpacity(toFade, inverted ? (float) (100 - query) / 100 : (float) query / 100));
    }
}
