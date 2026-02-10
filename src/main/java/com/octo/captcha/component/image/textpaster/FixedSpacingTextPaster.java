package com.octo.captcha.component.image.textpaster;

import com.octo.captcha.CaptchaException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

public class FixedSpacingTextPaster implements TextPaster {

    private final int minLength;
    private final int maxLength;
    private final Color color;
    private final Font font;

    public FixedSpacingTextPaster(int minLength, int maxLength, Color color, Font font) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.color = color;
        this.font = font;
    }

    @Override
    public int getMaxAcceptedWordLength() {
        return maxLength;
    }

    @Override
    public int getMinAcceptedWordLength() {
        return minLength;
    }

    @Override
    @Deprecated
    public int getMaxAcceptedWordLenght() {
        return maxLength;
    }

    @Override
    @Deprecated
    public int getMinAcceptedWordLenght() {
        return minLength;
    }

    @Override
    public BufferedImage pasteText(BufferedImage background, AttributedString attributedWord) throws CaptchaException {
        Graphics2D g2 = background.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setFont(font);

        String word = attributedWord.getIterator().toString();

        int x = 20;
        int y = background.getHeight() / 2 + font.getSize() / 2 - 5;
        int spacing = font.getSize() + 5;

        for (char c : word.toCharArray()) {
            g2.drawString(String.valueOf(c), x, y);
            x += spacing;
        }

        g2.dispose();
        return background;
    }
}
