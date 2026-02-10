package com.octo.captcha.image.fisheye;

import com.octo.captcha.image.ImageCaptcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

/**
 * FishEye è un ImageCaptcha con:
 * - Tipo di sfida: immagine
 * - Tipo di risposta: una posizione (Point) in pixel dal basso a sinistra
 * - Descrizione: un'immagine distorta. L'utente deve indicare il centro della deformazione.
 */
public class FishEye extends ImageCaptcha {

    private static final long serialVersionUID = 1L;

    private final Point deformationCenter;
    private final int tolerance;

    /**
     * Costruttore.
     *
     * @param question          la domanda associata al captcha
     * @param challenge         l'immagine del captcha
     * @param deformationCenter il centro della deformazione applicata all'immagine
     * @param tolerance         la distanza massima accettata dal centro della deformazione
     */
    public FishEye(String question, BufferedImage challenge,
                   Point deformationCenter, int tolerance) {
        super(question, challenge);
        this.deformationCenter = deformationCenter;
        this.tolerance = tolerance;
    }

    /**
     * Valida la risposta fornita dall'utente.
     *
     * @param response la risposta dell'utente, può essere un Point o una Stringa "x,y"
     * @return true se la risposta è corretta, false altrimenti
     */
    @Override
    public Boolean validateResponse(Object response) {
        Point point = null;

        if (response instanceof Point) {
            point = (Point) response;
        } else if (response instanceof String) {
            try {
                StringTokenizer token = new StringTokenizer((String) response, ",");
                int x = Integer.parseInt(token.nextToken().trim());
                int y = Integer.parseInt(token.nextToken().trim());
                point = new Point(x, y);
            } catch (Exception e) {
                return Boolean.FALSE;
            }
        }

        if (point != null) {
            return point.distance(deformationCenter) <= tolerance;
        }

        return Boolean.FALSE;
    }

    /**
     * Indica se il metodo getChallenge è stato chiamato.
     *
     * @return true se getChallenge è stato chiamato, false altrimenti
     */
    @Override
    public Boolean hasGetChalengeBeenCalled() {
        return super.hasGetChalengeBeenCalled();
    }
}
