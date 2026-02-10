package com.octo.captcha.component.image.wordtoimage;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.textpaster.TextPaster;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.text.AttributedString;

/**
 * Versione moderna della WordToImage che applica tre gruppi di filtri BufferedImageOp.
 */
public class BufferedOpComposedWordToImage extends ComposedWordToImage {

    private BufferedImageOp[] backgroundFilters;
    private BufferedImageOp[] textFilters;
    private BufferedImageOp[] postFilters;

    public BufferedOpComposedWordToImage(
            FontGenerator fontGenerator,
            BackgroundGenerator backgroundGenerator,
            TextPaster textPaster,
            BufferedImageOp[] backgroundFilters,
            BufferedImageOp[] textFilters,
            BufferedImageOp[] postFilters) {
        super(fontGenerator, backgroundGenerator, textPaster);
        this.backgroundFilters = backgroundFilters;
        this.textFilters = textFilters;
        this.postFilters = postFilters;
    }

    @Override
    public BufferedImage getImage(String word) throws CaptchaException {
        // Recupera lo sfondo base
        BufferedImage backgroundImage = background.getBackground();
        BufferedImage backgroundFiltered = applyFilters(backgroundImage, backgroundFilters);

        // Applica gli attributi alla parola
        AttributedString attributedWord = getAttributedString(word, fontGenerator.getMaxFontSize());

        // Applica il testo
        BufferedImage withText = textPaster.pasteText(backgroundFiltered, attributedWord);
        BufferedImage textFiltered = applyFilters(withText, textFilters);

        // Applica eventuali filtri post
        return applyFilters(textFiltered, postFilters);
    }

    private BufferedImage applyFilters(BufferedImage image, BufferedImageOp[] filters) {
        BufferedImage result = image;
        if (filters != null) {
            for (BufferedImageOp filter : filters) {
                if (filter != null) {
                    result = filter.filter(result, null);
                }
            }
        }
        return result;
    }
}
