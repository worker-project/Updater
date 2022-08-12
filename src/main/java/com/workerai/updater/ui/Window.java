package com.workerai.updater.ui;

import com.workerai.updater.WorkerUpdater;
import com.workerai.updater.ui.component.bar.ProgressBar;
import com.workerai.updater.ui.component.button.textured.TexturedButton;
import com.workerai.updater.ui.component.fade.FadeAnimation;
import com.workerai.updater.ui.component.utils.ButtonCreator;
import com.workerai.updater.utils.ResourceManager;
import fr.flowarg.flowcompat.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import static com.workerai.updater.ui.component.utils.ButtonCreator.createHoverButton;
import static com.workerai.updater.ui.component.utils.ProgressCreator.createProgressBar;
import static com.workerai.updater.utils.ColorManager.COFFEE;
import static com.workerai.updater.utils.ThrowWait.throwWait;

public class Window {
    private final JFrame startFrame;
    private final JFrame updateFrame;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ProgressBar progressBar;
    private final JLabel progressLabel = new JLabel("");

    public Window() {
        startFrame = new JFrame();
        updateFrame = new JFrame();

        setFrameSettings(this.startFrame);
        setFrameSettings(this.updateFrame);
    }

    void setFrameSettings(JFrame frame) {
        frame.setTitle("WorkerUpdater");

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(!Platform.isOnLinux());
    }

    void createFrame(JFrame frame, JPanel panel) {
        frame.setSize(600, 350);
        frame.setLocation(screenSize.width / 2 - 600 / 2, screenSize.height / 2 - 350 / 2);

        ImageIcon iconImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getFavIcon())));
        frame.setIconImage(iconImage.getImage());

        panel.setLayout(null);
        panel.setSize(600, 350);
        panel.setBackground(COFFEE);
    }

    void drawBackground(JPanel panel) {
        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getBackground())));
        JLabel backgroundLabel = new JLabel(backgroundImage);

        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        panel.add(backgroundLabel, BorderLayout.CENTER);
    }

    void drawLogo(JPanel panel) {
        ImageIcon logoImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(ResourceManager.getLogo())));
        JLabel logoLabel = new JLabel(logoImage);

        logoLabel.setBounds(0, 0, logoImage.getIconWidth(), logoImage.getIconHeight());
        panel.add(logoLabel, BorderLayout.CENTER);
    }

    void drawCloseButton(JPanel panel) {
        TexturedButton closeButton = (TexturedButton) createHoverButton("", 600 - 30 - 15, 15, 30, 30, ButtonCreator.BUTTON_TYPE.CLOSE_TEXTURED);
        panel.add(closeButton, null);
    }

    void drawFrame(JFrame frameON, JFrame frameOFF) {
        frameON.setVisible(true);
        frameOFF.setVisible(false);
    }

    void drawProgressBar(JPanel panel) {
        progressBar = createProgressBar("Download", 600 / 2 - 400 / 2, 305, 400, 30, 0);
        panel.add(progressBar, null);
    }

    void drawProgressText(JPanel panel) {
        progressLabel.setBounds(600 / 2 - 200 / 2, 250, 200, 80);
        progressLabel.setForeground(Color.RED);
        panel.add(progressLabel, BorderLayout.NORTH);
    }

    public void drawStartPage() {
        JPanel panel = new JPanel();
        this.createFrame(this.startFrame, panel);

        this.drawLogo(panel);
        this.drawCloseButton(panel);
        this.drawProgressText(panel);

        this.startFrame.setContentPane(panel);
        this.drawFrame(this.startFrame, this.updateFrame);

        FadeAnimation.fadeInFrame(this.startFrame, FadeAnimation.FAST);

        throwWait(3000);
        new WorkerUpdater();
    }

    public void drawUpdatePage() {
        JPanel panel = new JPanel();
        this.createFrame(this.updateFrame, panel);

        this.drawBackground(panel);
        this.drawProgressBar(panel);
        this.drawCloseButton(panel);
        this.drawProgressText(panel);

        this.updateFrame.setContentPane(panel);
        this.drawFrame(this.updateFrame, this.startFrame);

        FadeAnimation.fadeOutFrame(this.startFrame, FadeAnimation.FAST);
        FadeAnimation.fadeInFrame(this.updateFrame, FadeAnimation.FAST);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getProgressLabel() {
        return progressLabel;
    }
}
