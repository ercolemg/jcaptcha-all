package com.octo.captcha.engine.image.gimpy;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.BaffleTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.jhlabs.image.WaterFilter;

import java.awt.*;
import java.awt.image.BufferedImageOp;

public class DefaultGimpyEngine extends ListImageCaptchaEngine {

    @Override
    protected void buildInitialFactories() {
        // Configura il filtro water
        WaterFilter water = new WaterFilter();
        water.setAmplitude(3f);            // usa float invece di double
        water.setPhase(20f);
        water.setWavelength(70f);
        water.setEdgeAction(WaterFilter.CLAMP); // opzionale
        // NOTA: JHLabs WaterFilter in alcune versioni non ha setAntialias()

        // Deformazioni (usa BufferedImageOp)
        ImageDeformation backDef = new ImageDeformationByFilters(new BufferedImageOp[]{});
        ImageDeformation textDef = new ImageDeformationByFilters(new BufferedImageOp[]{});
        ImageDeformation postDef = new ImageDeformationByFilters(new BufferedImageOp[]{water});

        // Generatore di parole
        WordGenerator dictionaryWords = new ComposeDictionaryWordGenerator(
                new FileDictionary("toddlist")
        );

        // Generatore di testo decorato
        TextPaster randomPaster = new DecoratedRandomTextPaster(
                6, 7,
                new SingleColorGenerator(Color.BLACK),
                new TextDecorator[]{new BaffleTextDecorator(1, Color.WHITE)}
        );

        // Generatore sfondo e font
        BackgroundGenerator background = new UniColorBackgroundGenerator(200, 100, Color.WHITE);
        FontGenerator fontGenerator = new RandomFontGenerator(30, 35);

        // WordToImage
        DeformedComposedWordToImage wordToImage = new DeformedComposedWordToImage(
                fontGenerator,
                background,
                randomPaster,
                backDef,
                textDef,
                postDef
        );

        // Factory
        this.addFactory(new GimpyFactory(dictionaryWords, wordToImage));
    }
}
