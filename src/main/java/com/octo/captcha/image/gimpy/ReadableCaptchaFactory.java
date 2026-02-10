package com.octo.captcha.image.gimpy;

import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.FlatColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class ReadableCaptchaFactory implements CaptchaFactory {

    private final WordGenerator wordGenerator;
    private final ComposedWordToImage wordToImage;

    public ReadableCaptchaFactory() {
        this.wordGenerator = new RandomWordGenerator("ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789");

        var textPaster = new SimpleTextPaster(5, 6, Color.BLACK);
        var bg = new FlatColorBackgroundGenerator(250, 100, Color.WHITE);
        var fontGen = new RandomFontGenerator(40, 45);

        this.wordToImage = new ComposedWordToImage(fontGen, bg, textPaster);
    }

    @Override
    public Captcha getCaptcha() {
        return createCaptcha();
    }

    @Override
    public Captcha getCaptcha(Locale locale) {
        return createCaptcha();
    }

    private Captcha createCaptcha() {
        String word = wordGenerator.getWord(6);
        BufferedImage image = wordToImage.getImage(word);
        return new Gimpy(word, image, word); // il terzo argomento è la risposta
    }
}
