package com.workerai.updater.ui;

import com.workerai.updater.WorkerUpdater;
import com.workerai.updater.ui.component.button.colored.ColoredButton;
import com.workerai.updater.ui.component.button.textured.TexturedButton;
import com.workerai.updater.ui.component.fade.FadeAnimation;
import com.workerai.updater.ui.component.utils.ButtonCreator;
import com.workerai.updater.ui.component.bar.ProgressBar;
import com.workerai.updater.utils.ResourceManager;
import fr.flowarg.flowcompat.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static com.workerai.updater.ui.component.utils.ButtonCreator.createHoverButton;
import static com.workerai.updater.ui.component.utils.ProgressCreator.createProgressBar;
import static com.workerai.updater.utils.ColorManager.COFFEE;

public class Window {
    private final JFrame startFrame;
    private final JFrame updateFrame;
    private final JFrame endFrame;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ProgressBar progressBar;

    public Window() {
        startFrame = new JFrame();
        updateFrame = new JFrame();
        endFrame = new JFrame();

        setFrameSettings(this.startFrame);
        setFrameSettings(this.updateFrame);
        setFrameSettings(this.endFrame);
    }

    void setFrameSettings(JFrame frame) {
        frame.setTitle("WorkerUpdater");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(!Platform.isOnLinux());
    }

    void createFrame(JFrame frame) {
        frame.setSize(600, 350);
        frame.setLocation(screenSize.width / 2 - 600 / 2, screenSize.height / 2 - 350 / 2);

        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getFavIcon())));
        frame.setIconImage(backgroundImage.getImage());
        frame.getContentPane().setBackground(COFFEE);
    }

    void drawBackground(JFrame frame) {
        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getBackground())));
        JLabel backgroundLabel = new JLabel(backgroundImage);

        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        frame.getContentPane().add(backgroundLabel, BorderLayout.CENTER);
    }

    void drawLogo(JFrame frame) {
        ImageIcon logoImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getLogo())));
        JLabel logoLabel = new JLabel(logoImage);

        logoLabel.setBounds(0, 0, logoImage.getIconWidth(), logoImage.getIconHeight());
        frame.getContentPane().add(logoLabel, BorderLayout.CENTER);
    }

    void drawButton(JFrame frame) {
        TexturedButton closeButton = (TexturedButton) createHoverButton("", 600 - 30 - 15, 15, 30, 30, ButtonCreator.BUTTON_TYPE.CLOSE_TEXTURED);
        frame.getContentPane().add(closeButton, null);
    }

    void drawPlayButton(JFrame frame) {
        ColoredButton playButton = (ColoredButton) createHoverButton("Launch", 300 - 60, 285, 120, 45, ButtonCreator.BUTTON_TYPE.LAUNCH_COLORED);
        frame.getContentPane().add(playButton, null);
    }

    void drawFrame(JFrame frameON, JFrame frameOFF) {
        frameON.setVisible(true);
        frameOFF.setVisible(false);
    }

    public void drawStartPage() {
        this.createFrame(this.startFrame);

        this.startFrame.setLayout(null);
        this.drawButton(this.startFrame);
        this.drawLogo(this.startFrame);
        this.drawFrame(this.startFrame, this.updateFrame);

        FadeAnimation.fadeInFrame(this.startFrame, FadeAnimation.FAST);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new WorkerUpdater();
    }

    public void drawUpdatePage() {
        this.createFrame(this.updateFrame);

        this.updateFrame.setLayout(null);
        this.drawBackground(this.updateFrame);
        this.drawProgressBar(0, this.updateFrame);
        this.drawButton(this.updateFrame);
        this.drawFrame(this.updateFrame, this.startFrame);

        FadeAnimation.fadeOutFrame(this.startFrame, FadeAnimation.FAST);
        FadeAnimation.fadeInFrame(this.updateFrame, FadeAnimation.FAST);
    }

    public void drawEndPage() {
        this.createFrame(this.endFrame);

        this.endFrame.setLayout(null);
        this.drawButton(this.endFrame);
        this.drawLogo(this.endFrame);
        this.drawPlayButton(this.endFrame);
        //this.drawProgressBar(70, this.endFrame);
        this.drawFrame(this.endFrame, this.updateFrame);

        FadeAnimation.fadeOutFrame(this.updateFrame, FadeAnimation.FAST);
        FadeAnimation.fadeInFrame(this.endFrame, FadeAnimation.FAST);
    }

    void drawText(String text, JFrame frame) {
        JLabel label = new JLabel(text);
        frame.getContentPane().add(label);
    }

    void drawProgressBar(int value, JFrame frame) {
        progressBar = createProgressBar("Download", 600 / 2 - 400 / 2, 305, 400, 30, value);
        frame.getContentPane().add(progressBar, null);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
