package com.octo.captcha.engine.image.utils;

import com.octo.captcha.engine.image.CleanImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SimpleImageCaptchaToJPEG {

    public static void main(String[] args) {
        try {
        	CleanImageCaptchaEngine bge = new CleanImageCaptchaEngine();
            System.out.println("got gimpy");

            ImageCaptchaFactory factory = bge.getImageCaptchaFactory();
            System.out.println("got factory");

            ImageCaptcha pixCaptcha = factory.getImageCaptcha();
            System.out.println("got image");

            System.out.println(pixCaptcha.getQuestion());

            BufferedImage bi = pixCaptcha.getImageChallenge();

            File f = new File("foo.jpg");

            // Scrive l'immagine in formato JPEG utilizzando ImageIO
            boolean result = ImageIO.write(bi, "jpg", f);

            if (result) {
                System.out.println("Immagine salvata con successo in foo.jpg");
            } else {
                System.err.println("Errore: nessun writer trovato per il formato 'jpg'");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
