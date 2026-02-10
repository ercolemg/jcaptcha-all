package com.octo.captcha.engine.image.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Utility per scrivere un'immagine JPEG su file.
 */
public class ImageToFile {

    public ImageToFile() {
    }

    public static void serialize(BufferedImage image, File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(image, "jpg", file);
    }

    public static void encodeJPG(OutputStream os, BufferedImage image) throws IOException {
        ImageIO.write(image, "jpg", os);
    }
}
