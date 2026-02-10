package com.octo.captcha.component.image.deformation;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Use an array of BufferedImageOp (JHLabs or other filters) to deform an image.
 * Updated for Java 17 compatibility and modern image processing.
 *
 * @author Refactored
 */
public class ImageDeformationByFilters implements ImageDeformation {

    private BufferedImageOp[] filters;

    /**
     * Constructor with an array of BufferedImageOp.
     *
     * @param filters the filters to apply to the image
     */
    public ImageDeformationByFilters(BufferedImageOp[] filters) {
        this.filters = filters;
    }

    /**
     * Deforms an image by applying the sequence of BufferedImageOps.
     *
     * @param image the original image
     * @return the transformed image
     */
    @Override
    public BufferedImage deformImage(BufferedImage image) {
        if (filters == null || filters.length == 0) {
            return image;
        }

        BufferedImage result = image;
        for (BufferedImageOp filter : filters) {
            result = filter.filter(result, null);
        }
        return result;
    }
}
