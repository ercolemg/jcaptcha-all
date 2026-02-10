package com.octo.captcha.engine.image.gimpy;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.BaffleRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.BufferedOpComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.BufferedImageOp;

public class FilteredBaffleListGimpyEngine extends ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {

        // Filtri JH Labs
        com.jhlabs.image.EmbossFilter emboss = new com.jhlabs.image.EmbossFilter();
        com.jhlabs.image.SphereFilter sphere = new com.jhlabs.image.SphereFilter();
        com.jhlabs.image.RippleFilter rippleBack = new com.jhlabs.image.RippleFilter();
        com.jhlabs.image.RippleFilter ripple = new com.jhlabs.image.RippleFilter();
        com.jhlabs.image.TwirlFilter twirl = new com.jhlabs.image.TwirlFilter();
        com.jhlabs.image.WaterFilter water = new com.jhlabs.image.WaterFilter();
        com.jhlabs.image.MarbleFilter marble = new com.jhlabs.image.MarbleFilter();
        com.jhlabs.image.WeaveFilter weaves = new com.jhlabs.image.WeaveFilter();

        // Configura i filtri
        emboss.setBumpHeight(2.0f);

        ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
        ripple.setXAmplitude(3);
        ripple.setYAmplitude(3);
        ripple.setXWavelength(20);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);

        rippleBack.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
        rippleBack.setXAmplitude(5);
        rippleBack.setYAmplitude(5);
        rippleBack.setXWavelength(10);
        rippleBack.setYWavelength(10);
        rippleBack.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);

        water.setAmplitude(1f);
        water.setWavelength(20);

        twirl.setAngle(3f / 360f);

        sphere.setRefractionIndex(1);

        weaves.setUseImageColors(true);

        // Generatore di parole
        WordGenerator words = new DictionaryWordGenerator(
                new com.octo.captcha.component.word.FileDictionary("toddlist")
        );

        // Componenti per word-to-image
        TextPaster paster = new BaffleRandomTextPaster(6, 8, Color.black, 3, Color.white);
        BackgroundGenerator back = new UniColorBackgroundGenerator(200, 100, Color.white);
        FontGenerator font = new TwistedAndShearedRandomFontGenerator(30, 40);

        // Factory base senza filtri
        WordToImage word2image = new BufferedOpComposedWordToImage(
                font, back, paster, null, null, null
        );
        this.addFactory(new GimpyFactory(words, word2image));

        // Factory 1: water, emboss, ripple
        word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{water},
                new BufferedImageOp[]{emboss},
                new BufferedImageOp[]{ripple}
        );
        this.addFactory(new GimpyFactory(words, word2image));

        // Factory 2: rippleBack, marble, ripple
        word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{rippleBack},
                new BufferedImageOp[]{marble},
                new BufferedImageOp[]{ripple}
        );
        this.addFactory(new GimpyFactory(words, word2image));

        // Factory 3: rippleBack, (nessun filtro centrale), weaves
        word2image = new BufferedOpComposedWordToImage(
                font, back, paster,
                new BufferedImageOp[]{rippleBack},
                null,
                new BufferedImageOp[]{weaves}
        );
        this.addFactory(new GimpyFactory(words, word2image));
    }
}
