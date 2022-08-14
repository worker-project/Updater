package com.workerai.updater.ui.component.button;

import com.workerai.updater.ui.component.button.event.ButtonEvent;
import com.workerai.updater.ui.component.button.event.ButtonEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class AbstractButton extends JComponent implements MouseListener {
    private String text;

    private Color textColor;

    private final ArrayList<ButtonEventListener> eventListeners = new ArrayList<ButtonEventListener>();

    private boolean hover = false;

    public AbstractButton() {
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(this.isEnabled())
            for(ButtonEventListener eventListener : this.eventListeners)
                eventListener.onEvent(new ButtonEvent(this, ButtonEvent.BUTTON_CLICKED_EVENT));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hover = true;

        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hover = false;

        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        repaint();
    }

    public void setText(String text) {
        if(text == null)
            throw new IllegalArgumentException("text == null");
        this.text = text;

        repaint();
    }

    public String getText() {
        return text;
    }

    public void setTextColor(Color textColor) {
        if(textColor == null)
            throw new IllegalArgumentException("textColor == null");
        this.textColor = textColor;

        repaint();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void addEventListener(ButtonEventListener eventListener) {
        if(eventListener == null)
            throw new IllegalArgumentException("eventListener == null");

        this.eventListeners.add(eventListener);
    }

    public ArrayList<ButtonEventListener> getEventListeners() {
        return this.eventListeners;
    }

    public boolean isHover() {
        return this.hover;
    }
}
