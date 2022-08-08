package com.workerai.updater.ui.component.button.textured;

import com.workerai.updater.ui.component.button.AbstractButton;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.workerai.updater.ui.component.DrawComponent.*;

public class TexturedButton extends AbstractButton {
    private Image texture;

    private Image textureHover;

    private Image textureDisabled;

    public TexturedButton(BufferedImage texture) {
        this(texture, null, null);
    }

    public TexturedButton(BufferedImage texture, BufferedImage textureHover) {
        this(texture, textureHover, null);
    }

    public TexturedButton(BufferedImage texture, BufferedImage textureHover, BufferedImage textureDisabled) {
        super();

        if (texture == null)
            throw new IllegalArgumentException("texture == null");
        this.texture = texture;

        if (textureHover == null)
            this.textureHover = fillImage(copyImage(texture), HOVER_COLOR, this.getParent());
        else
            this.textureHover = textureHover;

        if (textureDisabled == null)
            this.textureDisabled = fillImage(copyImage(texture), DISABLED_COLOR, this.getParent());
        else
            this.textureDisabled = textureDisabled;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image texture;
        if (!this.isEnabled())
            texture = textureDisabled;
        else if (super.isHover())
            texture = textureHover;
        else
            texture = this.texture;

        drawFullsizedImage(g, this, texture);

        if (getText() != null) {
            activateAntialias(g);

            if (getTextColor() != null)
                g.setColor(getTextColor());

            drawCenteredString(g, getText(), this.getBounds());
        }
    }

    public void setTexture(Image texture) {
        if (texture == null)
            throw new IllegalArgumentException("texture == null");
        this.texture = texture;

        repaint();
    }

    public void setTextureHover(Image textureHover) {
        if (textureHover == null)
            throw new IllegalArgumentException("textureHover == null");
        this.textureHover = textureHover;

        repaint();
    }

    public void setTextureDisabled(Image textureDisabled) {
        if (textureDisabled == null)
            throw new IllegalArgumentException("textureDisabled == null");
        this.textureDisabled = textureDisabled;

        repaint();
    }

    public Image getTexture() {
        return this.texture;
    }

    public Image getTextureHover() {
        return this.textureHover;
    }

    public Image getTextureDisabled() {
        return this.textureDisabled;
    }

    public void setBounds(int x, int y) {
        this.setBounds(x, y, this.texture.getWidth(this.getParent()), this.texture.getHeight(this.getParent()));
    }
}
