package com.octo.captcha.engine.image.gimpy;

import com.jhlabs.image.*;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.BufferedOpComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.BufferedImageOp;

public class FilteredGimpyEngine extends com.octo.captcha.engine.image.DefaultImageCaptchaEngine {

    static ImageCaptchaFactory[] factories;

    static {
        // Filters
        EmbossFilter emboss = new EmbossFilter();
        SphereFilter sphere = new SphereFilter();
        RippleFilter rippleBack = new RippleFilter();
        RippleFilter ripple = new RippleFilter();
        TwirlFilter twirl = new TwirlFilter();
        WaterFilter water = new WaterFilter();
        MarbleFilter marble = new MarbleFilter();
        WeaveFilter weaves = new WeaveFilter();
        CrystallizeFilter crystal = new CrystallizeFilter();

        emboss.setBumpHeight(2.0f);

        ripple.setWaveType(RippleFilter.NOISE);
        ripple.setXAmplitude(10);
        ripple.setYAmplitude(3);
        ripple.setXWavelength(20);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(RippleFilter.CLAMP);

        rippleBack.setWaveType(RippleFilter.NOISE);
        rippleBack.setXAmplitude(5);
        rippleBack.setYAmplitude(5);
        rippleBack.setXWavelength(10);
        rippleBack.setYWavelength(10);
        rippleBack.setEdgeAction(RippleFilter.CLAMP);

        water.setAmplitude(5f);
        water.setWavelength(10f);

        twirl.setAngle((float) (3 * Math.PI / 180)); // 3 degrees in radians

        sphere.setRefractionIndex(1f);

        weaves.setUseImageColors(true);

        crystal.setScale(0.5f);
        crystal.setGridType(CrystallizeFilter.RANDOM);
        crystal.setFadeEdges(false);
        crystal.setEdgeThickness(0.2f);
        crystal.setRandomness(0.1f);

        // Componenti CAPTCHA
        TextPaster paster = new RandomTextPaster(8, 10, Color.gray);
        BackgroundGenerator back = new FunkyBackgroundGenerator(200, 100);
        FontGenerator font = new RandomFontGenerator(25, 35);
        WordGenerator words = new DictionaryWordGenerator(
                new com.octo.captcha.component.word.FileDictionary("toddlist"));

        factories = new ImageCaptchaFactory[3];

        // Factory 1
        WordToImage word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{water},
                new BufferedImageOp[]{emboss},
                new BufferedImageOp[]{ripple}
        );
        factories[0] = new GimpyFactory(words, word2image);

        // Factory 2
        word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{rippleBack},
                new BufferedImageOp[]{crystal},
                new BufferedImageOp[]{ripple}
        );
        factories[1] = new GimpyFactory(words, word2image);

        // Factory 3
        word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{rippleBack},
                null,
                new BufferedImageOp[]{weaves}
        );
        factories[2] = new GimpyFactory(words, word2image);
    }

    public FilteredGimpyEngine() {
        super(factories);
    }
}
