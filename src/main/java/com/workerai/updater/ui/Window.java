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
import static com.workerai.updater.utils.ColorManager.DARK_YELLOW;
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
        progressBar = createProgressBar("Download", 600 / 2 - 400 / 2, 290, 400, 30, 0, 80);
        panel.add(progressBar, 2,0);
    }

    void drawProgressText(JPanel panel) {
        progressTextFormatter();
        progressLabel.setForeground(DARK_YELLOW);
        panel.add(progressLabel, 2, 0);
    }

    public void progressTextFormatter() {
        FontMetrics fm = progressLabel.getFontMetrics(progressLabel.getFont());
        int width = fm.stringWidth(progressLabel.getText());
        progressLabel.setBounds(600 / 2 - width / 2 - 5, 295, 280, 80);
    }

    public void drawStartPage() {
        JPanel startPanel = new JPanel();
        this.createFrame(this.startFrame, startPanel);

        this.drawLogo(startPanel);
        this.drawCloseButton(startPanel);

        this.startFrame.setContentPane(startPanel);
        this.drawFrame(this.startFrame, this.updateFrame);

        FadeAnimation.fadeInFrame(this.startFrame, FadeAnimation.FAST);

        throwWait(3000);
        new WorkerUpdater();
    }

    public void drawUpdatePage() {
        JPanel updatePanel = new JPanel();
        this.createFrame(this.updateFrame, updatePanel);

        this.drawBackground(updatePanel);
        this.drawProgressBar(updatePanel);
        this.drawCloseButton(updatePanel);
        this.drawProgressText(updatePanel);

        this.updateFrame.setContentPane(updatePanel);
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
