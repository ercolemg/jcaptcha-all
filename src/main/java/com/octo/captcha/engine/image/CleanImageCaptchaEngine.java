package com.octo.captcha.engine.image;

import com.octo.captcha.component.image.backgroundgenerator.FlatColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.Color;

public class CleanImageCaptchaEngine extends ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {
        var wordGenerator = new RandomWordGenerator("ABCDEFGHJKLMNPQRSTUVWXYZ23456789");

        var textPaster = new SimpleTextPaster(5, 6, Color.BLACK); // no decorazioni

        var backgroundGenerator = new FlatColorBackgroundGenerator(250, 100, Color.WHITE);

        var fontGenerator = new RandomFontGenerator(40, 45); // font leggibili

        var wordToImage = new ComposedWordToImage(
                fontGenerator,
                backgroundGenerator,
                textPaster
        );

        addFactory(new GimpyFactory(wordGenerator, wordToImage));
    }
}
