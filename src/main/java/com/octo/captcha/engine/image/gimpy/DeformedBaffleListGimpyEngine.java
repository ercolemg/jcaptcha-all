package com.octo.captcha.engine.image.gimpy;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByBufferedOps;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.BaffleRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.BufferedImageOp;

import com.jhlabs.image.*;

public class DeformedBaffleListGimpyEngine extends ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {
        // Inizializza filtri jhlabs
        RippleFilter ripple = new RippleFilter();
        ripple.setWaveType(RippleFilter.NOISE);
        ripple.setXAmplitude(3f);
        ripple.setYAmplitude(3f);
        ripple.setXWavelength(20);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(TransformFilter.CLAMP);

        RippleFilter rippleBack = new RippleFilter();
        rippleBack.setWaveType(RippleFilter.NOISE);
        rippleBack.setXAmplitude(5f);
        rippleBack.setYAmplitude(5f);
        rippleBack.setXWavelength(10);
        rippleBack.setYWavelength(10);
        rippleBack.setEdgeAction(TransformFilter.CLAMP);

        WaterFilter water = new WaterFilter();
        water.setAmplitude(1f);
        water.setWavelength(20f);

        EmbossFilter emboss = new EmbossFilter();

        SphereFilter sphere = new SphereFilter();
        sphere.setRefractionIndex(1f);

        TwirlFilter twirl = new TwirlFilter();
        twirl.setAngle((float) (3 * Math.PI / 180)); // 3 gradi in radianti

        WeaveFilter weaves = new WeaveFilter();
        weaves.setUseImageColors(true);

        CrystallizeFilter crystal = new CrystallizeFilter();
        crystal.setScale(0.5f);
        crystal.setGridType(CrystallizeFilter.RANDOM);
        crystal.setFadeEdges(false);
        crystal.setEdgeThickness(0.2f);
        crystal.setRandomness(0.1f);

        // Converto i filtri in BufferedImageOp[]
        ImageDeformation rippleDef = new ImageDeformationByBufferedOps(new BufferedImageOp[]{ripple});
        ImageDeformation waterDef = new ImageDeformationByBufferedOps(new BufferedImageOp[]{water});
        ImageDeformation embossDef = new ImageDeformationByBufferedOps(new BufferedImageOp[]{emboss});
        ImageDeformation rippleDefBack = new ImageDeformationByBufferedOps(new BufferedImageOp[]{rippleBack});
        ImageDeformation cristalDef = new ImageDeformationByBufferedOps(new BufferedImageOp[]{crystal});
        ImageDeformation weavesDef = new ImageDeformationByBufferedOps(new BufferedImageOp[]{weaves});
        ImageDeformation none = new ImageDeformationByBufferedOps(null);

        // word generator
        WordGenerator words = new DictionaryWordGenerator(
                new com.octo.captcha.component.word.FileDictionary("toddlist"));

        // word2image components
        TextPaster paster = new BaffleRandomTextPaster(6, 8, Color.black, 3, Color.white);
        BackgroundGenerator back = new UniColorBackgroundGenerator(200, 100, Color.white);
        FontGenerator font = new TwistedAndShearedRandomFontGenerator(30, 40);

        // Factory 1: no deformation
        WordToImage word2image = new ComposedWordToImage(font, back, paster);
        addFactory(new GimpyFactory(words, word2image));

        // Factory 2: ripple + water + emboss
        word2image = new DeformedComposedWordToImage(font, back, paster, rippleDef, waterDef, embossDef);
        addFactory(new GimpyFactory(words, word2image));

        // Factory 3: rippleBack + crystal + ripple
        word2image = new DeformedComposedWordToImage(font, back, paster, rippleDefBack, cristalDef, rippleDef);
        addFactory(new GimpyFactory(words, word2image));

        // Factory 4: rippleBack + none + weaves
        word2image = new DeformedComposedWordToImage(font, back, paster, rippleDefBack, none, weavesDef);
        addFactory(new GimpyFactory(words, word2image));
    }
}
