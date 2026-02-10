package com.octo.captcha.engine.image.fisheye;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.image.WaterFilter;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.fisheye.FishEyeFactory;

import java.awt.image.BufferedImageOp;

/**
 * Produce fishEye from files. FishEye are done from sphere
 *
 * @author Marc-Antoine Garrigue
 * @version 1.0
 */
public class SimpleFishEyeEngine extends ListImageCaptchaEngine {

    /**
     * This method initializes the captcha factories with different image deformations.
     */
    @Override
    protected void buildInitialFactories() {
        // JH Labs filters
        SphereFilter sphere = new SphereFilter();
        RippleFilter ripple = new RippleFilter();
        TwirlFilter twirl = new TwirlFilter();
        WaterFilter water = new WaterFilter();

        // Configure RippleFilter
        ripple.setWaveType(RippleFilter.NOISE);
        ripple.setXAmplitude(10);
        ripple.setYAmplitude(10);
        ripple.setXWavelength(10);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(TransformFilter.CLAMP);

        // Configure WaterFilter
        water.setAmplitude(10);
        water.setWavelength(20);
        // water.setAntialias(true); // <- not supported in current version

        // Configure TwirlFilter
        twirl.setAngle(4);

        // Configure SphereFilter
        sphere.setRefractionIndex(2);

        // Wrap filters in deformations
        ImageDeformation rippleDef = new ImageDeformationByFilters(new BufferedImageOp[]{ripple});
        ImageDeformation sphereDef = new ImageDeformationByFilters(new BufferedImageOp[]{sphere});
        ImageDeformation waterDef = new ImageDeformationByFilters(new BufferedImageOp[]{water});
        ImageDeformation twirlDef = new ImageDeformationByFilters(new BufferedImageOp[]{twirl});

        // Background generator
        BackgroundGenerator generator = new FileReaderRandomBackgroundGenerator(
                250, 250, "./fisheyebackgrounds");

        // Add factories with each deformation
        addFactory(new FishEyeFactory(generator, sphereDef, 10, 5));
        addFactory(new FishEyeFactory(generator, rippleDef, 10, 5));
        addFactory(new FishEyeFactory(generator, waterDef, 10, 5));
        addFactory(new FishEyeFactory(generator, twirlDef, 10, 5));
    }
}
