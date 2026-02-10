package com.octo.captcha.image;

import com.octo.captcha.Captcha;
import java.awt.image.BufferedImage;

public abstract class ImageCaptcha implements Captcha {

    private Boolean hasChallengeBeenCalled = Boolean.FALSE;

    protected String question;
    protected transient BufferedImage challenge;

    protected ImageCaptcha(String question, BufferedImage challenge) {
        this.challenge = challenge;
        this.question = question;
    }

    public final String getQuestion() {
        return question;
    }

    public final Object getChallenge() {
        return getImageChallenge();
    }

    public final BufferedImage getImageChallenge() {
        hasChallengeBeenCalled = Boolean.TRUE;
        return challenge;
    }

    public final void disposeChallenge() {
        this.challenge = null;
    }

    @Override
    public Boolean hasGetChalengeBeenCalled() {
        return hasChallengeBeenCalled;
    }
}
