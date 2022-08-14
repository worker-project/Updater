package com.workerai.updater.ui.component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Objects;

public final class DrawComponent {
    public static final int LITTLE_TRANSPARENT = 50;

    public static final Color LITTLE_TRANSPARENT_WHITE = getTransparentWhite(LITTLE_TRANSPARENT);

    public static final Color HOVER_COLOR = LITTLE_TRANSPARENT_WHITE;

    public static final Color DISABLED_COLOR = getTransparentInstance(Color.GRAY, LITTLE_TRANSPARENT);

    public static Color getTransparentWhite(int transparency) {
        return getTransparentInstance(Color.WHITE, transparency);
    }

    public static Color getTransparentInstance(Color color, int transparency) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency);
    }

    public static BufferedImage copyImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static Image fillImage(Image image, Color color, ImageObserver imageObserver) {
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(imageObserver), image.getHeight(imageObserver));

        return image;
    }

    public static BufferedImage getResource(String resource) {
        try {
            return ImageIO.read(Objects.requireNonNull(DrawComponent.class.getResourceAsStream(resource)));
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Can't load the given resource (" + resource + ") : " + e);
        }
    }


    public static int crossMult(int value, int maximum, int coefficient) {
        return (int) ((double) value / (double) maximum * (double) coefficient);
    }

    public static Point getStringCenterPos(Rectangle parent, String str, FontMetrics fontMetrics, Graphics g) {
        Rectangle2D stringBounds = fontMetrics.getStringBounds(str, g);

        double x = ((parent.getWidth() - stringBounds.getWidth()) / 2);
        double y = ((parent.getHeight() - stringBounds.getHeight()) / 2 + fontMetrics.getAscent());
        return new Point((int) x, (int) y);
    }

    public static void drawCenteredString(Graphics g, String str, Rectangle parent) {
        FontMetrics fm = g.getFontMetrics();

        Point centerPos = getStringCenterPos(parent, str, fm, g);

        g.drawString(str, (int) centerPos.getX(), (int) centerPos.getY());
    }

    public static void activateAntialias(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    public static void drawFullsizedImage(Graphics g, JComponent component, Image image) {
        g.drawImage(image, 0, 0, component.getWidth(), component.getHeight(), component);
    }

    public static void fillFullsizedRect(Graphics g, JComponent component) {
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
    }

    public static void fillFullsizedRect(Graphics g, JComponent component, Color color) {
        g.setColor(color);
        g.fillRoundRect(0, 0, component.getWidth() - 1, component.getHeight() - 1, 15, 15);
    }
}
