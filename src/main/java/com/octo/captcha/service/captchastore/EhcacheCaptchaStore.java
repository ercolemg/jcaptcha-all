package com.octo.captcha.service.captchastore;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;

import java.util.Collections;
import java.util.Locale;

/**
 * Ehcache 3.x implementation of the CaptchaStore.
 * Accepts an externally managed cache.
 */
public class EhcacheCaptchaStore implements CaptchaStore {

    private static final Log log = LogFactory.getLog(EhcacheCaptchaStore.class);

    private final Cache<String, CaptchaAndLocale> cache;

    /**
     * Constructs a CaptchaStore backed by an Ehcache 3.x Cache.
     *
     * @param cache an externally managed cache instance
     */
    public EhcacheCaptchaStore(Cache<String, CaptchaAndLocale> cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache must not be null");
        }
        this.cache = cache;
    }

    @Override
    public boolean hasCaptcha(String id) {
        try {
            return cache.containsKey(id);
        } catch (Exception e) {
            log.error("Errore durante hasCaptcha()", e);
            return false;
        }
    }

    @Override
    public void storeCaptcha(String id, Captcha captcha) throws CaptchaServiceException {
        try {
            cache.put(id, new CaptchaAndLocale(captcha));
        } catch (Exception e) {
            log.error("Errore durante storeCaptcha(id, captcha)", e);
            throw new CaptchaServiceException(e);
        }
    }

    @Override
    public void storeCaptcha(String id, Captcha captcha, Locale locale) throws CaptchaServiceException {
        try {
            cache.put(id, new CaptchaAndLocale(captcha, locale));
        } catch (Exception e) {
            log.error("Errore durante storeCaptcha(id, captcha, locale)", e);
            throw new CaptchaServiceException(e);
        }
    }

    @Override
    public Captcha getCaptcha(String id) throws CaptchaServiceException {
        try {
            CaptchaAndLocale cal = cache.get(id);
            return cal != null ? cal.getCaptcha() : null;
        } catch (Exception e) {
            log.error("Errore durante getCaptcha()", e);
            throw new CaptchaServiceException(e);
        }
    }

    @Override
    public Locale getLocale(String id) throws CaptchaServiceException {
        try {
            CaptchaAndLocale cal = cache.get(id);
            return cal != null ? cal.getLocale() : null;
        } catch (Exception e) {
            log.error("Errore durante getLocale()", e);
            throw new CaptchaServiceException(e);
        }
    }

    @Override
    public boolean removeCaptcha(String id) {
        try {
            if (cache.containsKey(id)) {
                cache.remove(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Errore durante removeCaptcha()", e);
            return false;
        }
    }

    @Override
    public int getSize() {
        log.warn("getSize() non supportato direttamente in Ehcache 3.x");
        return -1;
    }

    @Override
    public java.util.Collection<String> getKeys() {
        log.warn("getKeys() non supportato in Ehcache 3.x");
        return Collections.emptyList();
    }

    @Override
    public void empty() {
        log.warn("empty() non supportato direttamente in Ehcache 3.x");
        // Soluzioni alternative:
        // 1. Rimozione chiavi manuale (se vengono tracciate separatamente)
        // 2. Cache ricreata esternamente dal CacheManager
    }
}
