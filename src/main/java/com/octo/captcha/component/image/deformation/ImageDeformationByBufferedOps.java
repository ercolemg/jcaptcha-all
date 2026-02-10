package com.octo.captcha.component.image.deformation;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Implementation of ImageDeformation using BufferedImageOp filters (e.g. from jhlabs).
 */
public class ImageDeformationByBufferedOps implements ImageDeformation {

    private final BufferedImageOp[] filters;

    public ImageDeformationByBufferedOps(BufferedImageOp[] filters) {
        this.filters = filters;
    }

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
