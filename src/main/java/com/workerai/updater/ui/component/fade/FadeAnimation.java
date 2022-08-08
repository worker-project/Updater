package com.workerai.updater.ui.component.fade;

import com.sun.awt.AWTUtilities;
import java.awt.Window;

public class FadeAnimation {
    public static final int SLOW = 20;
    public static final int NORMAL = 10;
    public static final int FAST = 5;

    public static void query(final long to, final LoopAction loopAction) {
        Thread t = new Thread(() -> {
            for (long query = 0; query < to + 1; query += 1)
                loopAction.onLoop(query);
        });
        t.start();
    }

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

    public static void query(final long from, final long to, final long toWait, final LoopAction loopAction) {
        Thread t = new Thread(() -> {
            for (long query = from; query < to + 1; query += 1) {
                loopAction.onLoop(query);
                try {
                    Thread.sleep(toWait);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
    }

    public static void query(final long from, final long to, final long speed, final long toWait, final LoopAction loopAction) {
        Thread t = new Thread(() -> {
            for (long query = from; query < to + 1; query += speed) {
                loopAction.onLoop(query);
                try {
                    Thread.sleep(toWait);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.start();
    }

    public static void fadeInFrame(Window toFade) {
        fade(toFade, NORMAL, false, null);
    }

    public static void fadeInFrame(Window toFade, Runnable callback) {
        fade(toFade, NORMAL, false, callback);
    }

    public static void fadeInFrame(Window toFade, int speed) {
        fade(toFade, speed, false, null);
    }

    public static void fadeInFrame(Window toFade, int speed, Runnable callback) {
        fade(toFade, speed, false, callback);
    }

    public static void fadeOutFrame(Window toFade) {
        fade(toFade, NORMAL, true, null);
    }

    public static void fadeOutFrame(Window toFade, Runnable callback) {
        fade(toFade, NORMAL, true, callback);
    }

    public static void fadeOutFrame(Window toFade, int speed) {
        fade(toFade, speed, true, null);
    }

    public static void fadeOutFrame(Window toFade, int speed, Runnable callback) {
        fade(toFade, speed, true, callback);
    }

    private static void fade(final Window toFade, final int speed, final boolean inverted, final Runnable callback) {
        query(100L, speed, query -> {
            AWTUtilities.setWindowOpacity(toFade, inverted ? (float) (100 - query) / 100 : (float) query / 100);
            if (query == 100L)
                if (callback != null)
                    callback.run();
        });
    }

}
