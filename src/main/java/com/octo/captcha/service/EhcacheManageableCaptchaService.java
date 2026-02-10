package com.octo.captcha.service;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.EhcacheCaptchaStore;
import com.octo.captcha.service.captchastore.MapCaptchaStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.Locale;

/**
 * Implementation of a ManageableCaptchaService using Ehcache 3.x.
 */
public abstract class EhcacheManageableCaptchaService extends AbstractCaptchaService
        implements EhcacheManageableCaptchaServiceMBean {

    private static final Log log = LogFactory.getLog(EhcacheManageableCaptchaService.class);

    private static final String DEFAULT_CACHE_ALIAS = "jcaptchaCache";

    private final CacheManager cacheManager;
    private final Cache<String, CaptchaAndLocale> captchaCache;

    private int minGuarantedStorageDelayInSeconds;
    private int captchaStoreMaxSize;

    private long numberOfGeneratedCaptchas = 0;
    private long numberOfCorrectResponse = 0;
    private long numberOfUncorrectResponse = 0;

    protected EhcacheManageableCaptchaService(CaptchaEngine captchaEngine,
                                              int minGuarantedStorageDelayInSeconds,
                                              int maxCaptchaStoreSize) {
        this(captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, DEFAULT_CACHE_ALIAS);
    }

    protected EhcacheManageableCaptchaService(CaptchaEngine captchaEngine,
                                              int minGuarantedStorageDelayInSeconds,
                                              int maxCaptchaStoreSize,
                                              String cacheAlias) {
        // Chiamata temporanea a MapCaptchaStore per inizializzare super
        super(new MapCaptchaStore(), captchaEngine);

        this.minGuarantedStorageDelayInSeconds = minGuarantedStorageDelayInSeconds;
        this.captchaStoreMaxSize = maxCaptchaStoreSize;

        // Inizializza Ehcache 3 CacheManager
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(cacheAlias,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                CaptchaAndLocale.class,
                                ResourcePoolsBuilder.heap(maxCaptchaStoreSize)
                        )
                ).build(true);

        this.captchaCache = cacheManager.getCache(cacheAlias, String.class, CaptchaAndLocale.class);

        // Sostituisce il CaptchaStore con EhcacheCaptchaStore
        super.store = new EhcacheCaptchaStore(this.captchaCache);
    }

    @Override
    public String getCaptchaEngineClass() {
        return this.engine.getClass().getName();
    }

    @Override
    public void setCaptchaEngineClass(String className) {
        try {
            Object instance = Class.forName(className).getDeclaredConstructor().newInstance();
            if (instance instanceof CaptchaEngine) {
                this.engine = (CaptchaEngine) instance;
            } else {
                throw new IllegalArgumentException("Class is not a CaptchaEngine: " + className);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error instantiating engine: " + e.getMessage(), e);
        }
    }

    @Override
    public int getMinGuarantedStorageDelayInSeconds() {
        return minGuarantedStorageDelayInSeconds;
    }

    @Override
    public void setMinGuarantedStorageDelayInSeconds(int seconds) {
        this.minGuarantedStorageDelayInSeconds = seconds;
        // NOTA: Ehcache 3.x non supporta la modifica del TTL runtime
    }

    @Override
    public long getNumberOfGeneratedCaptchas() {
        return numberOfGeneratedCaptchas;
    }

    @Override
    public long getNumberOfCorrectResponses() {
        return numberOfCorrectResponse;
    }

    @Override
    public long getNumberOfUncorrectResponses() {
        return numberOfUncorrectResponse;
    }

    @Override
    public int getCaptchaStoreSize() {
        return this.store.getSize();
    }

    @Override
    public int getNumberOfGarbageCollectableCaptchas() {
        return 0; // Non supportato da Ehcache 3.x
    }

    @Override
    public long getNumberOfGarbageCollectedCaptcha() {
        return 0; // Non supportato da Ehcache 3.x
    }

    @Override
    public int getCaptchaStoreSizeBeforeGarbageCollection() {
        return 0;
    }

    @Override
    public void setCaptchaStoreSizeBeforeGarbageCollection(int size) {
        throw new UnsupportedOperationException("Non supportato in Ehcache 3.x");
    }

    @Override
    public void setCaptchaStoreMaxSize(int size) {
        throw new UnsupportedOperationException("Runtime resize non supportato in Ehcache 3.x");
    }

    @Override
    public int getCaptchaStoreMaxSize() {
        return this.captchaStoreMaxSize;
    }

    @Override
    public void garbageCollectCaptchaStore() {
        log.warn("Garbage collection manuale non supportata in Ehcache 3.x");
    }

    @Override
    public void emptyCaptchaStore() {
        this.store.empty();
    }

    @Override
    protected Captcha generateAndStoreCaptcha(Locale locale, String id) {
        if (store.getSize() >= this.captchaStoreMaxSize) {
            throw new CaptchaServiceException("Store pieno. Aumenta la dimensione massima o riduci la TTL.");
        }

        Captcha captcha = this.engine.getNextCaptcha(locale);
        numberOfGeneratedCaptchas++;
        store.storeCaptcha(id, captcha, locale);
        return captcha;
    }

    @Override
    public Boolean validateResponseForID(String id, Object response) throws CaptchaServiceException {
        Boolean valid = super.validateResponseForID(id, response);
        if (Boolean.TRUE.equals(valid)) {
            numberOfCorrectResponse++;
        } else {
            numberOfUncorrectResponse++;
        }
        return valid;
    }
}
