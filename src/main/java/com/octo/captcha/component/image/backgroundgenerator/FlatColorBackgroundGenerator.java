package com.octo.captcha.component.image.backgroundgenerator;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Generatore di sfondo piatto con un colore uniforme.
 */
public class FlatColorBackgroundGenerator implements BackgroundGenerator {

    private int width;
    private int height;
    private Color color;

    public FlatColorBackgroundGenerator(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color != null ? color : Color.WHITE;
    }

    @Override
    public int getImageHeight() {
        return height;
    }

    @Override
    public int getImageWidth() {
        return width;
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(color);
            graphics.fillRect(0, 0, width, height);
        } finally {
            graphics.dispose();
        }
        return image;
    }
}
