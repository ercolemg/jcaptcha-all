package com.octo.captcha.engine.image.gimpy;

import com.jhlabs.image.RippleFilter;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.MultipleShapeBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.DeformedRandomFontGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.textpaster.DoubleRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.BufferedOpComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.BufferedImageOp;

public class FilteredDoubleRandomListGimpyEngine extends com.octo.captcha.engine.image.ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {

        // Filtro ripple come BufferedImageOp
        RippleFilter rippleBack = new RippleFilter();
        rippleBack.setWaveType(RippleFilter.NOISE);
        rippleBack.setXAmplitude(5);
        rippleBack.setYAmplitude(5);
        rippleBack.setXWavelength(10);
        rippleBack.setYWavelength(10);
        rippleBack.setEdgeAction(RippleFilter.CLAMP);

        TextPaster paster = new DoubleRandomTextPaster(8, 15, Color.black);
        BackgroundGenerator back = new MultipleShapeBackgroundGenerator(200, 100);
        FontGenerator font = new DeformedRandomFontGenerator(25, 27);
        WordGenerator words = new DictionaryWordGenerator(
                new com.octo.captcha.component.word.FileDictionary("toddlist"));

        WordToImage word2image = new BufferedOpComposedWordToImage(
                font,
                back,
                paster,
                new BufferedImageOp[]{rippleBack}, // background filters
                null,                              // text filters
                null                               // post filters
        );

        ImageCaptchaFactory factory = new GimpyFactory(words, word2image);
        this.addFactory(factory);
    }
}
