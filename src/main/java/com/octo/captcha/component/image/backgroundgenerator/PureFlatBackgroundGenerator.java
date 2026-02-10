package com.octo.captcha.component.image.backgroundgenerator;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PureFlatBackgroundGenerator extends AbstractBackgroundGenerator {

    private final Color backgroundColor;

    public PureFlatBackgroundGenerator(Integer width, Integer height, Color color) {
        super(width, height);
        this.backgroundColor = color;
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, getImageWidth(), getImageHeight());
        g2.dispose();
        return image;
    }

    // Metodo ausiliario: NON è override, ma utile in alcuni contesti
    public BufferedImage getBackground(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return image;
    }
}
