package com.octo.captcha.engine.bufferedengine.manager;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.engine.bufferedengine.ContainerConfiguration;
import com.octo.captcha.engine.bufferedengine.QuartzBufferedEngineContainer;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

public class QuartzBufferedEngineManager implements BufferedEngineContainerManager {
    private static final Log log = LogFactory.getLog(QuartzBufferedEngineManager.class.getName());

    private Scheduler scheduler;
    private CronTrigger cronFeeder;
    private CronTrigger cronSwapper;
    private QuartzBufferedEngineContainer container;
    private ContainerConfiguration config;
    private JobDetail jobFeeder;
    private JobDetail jobSwapper;

    public QuartzBufferedEngineManager(QuartzBufferedEngineContainer container,
                                       Scheduler scheduler, CronTrigger cronFeeder, CronTrigger cronSwapper,
                                       JobDetail jobFeeder, JobDetail jobSwapper) {
        this.cronFeeder = cronFeeder;
        this.cronSwapper = cronSwapper;
        this.jobFeeder = jobFeeder;
        this.jobSwapper = jobSwapper;
        this.scheduler = scheduler;
        this.container = container;
        this.config = container.getConfig();
    }

    public synchronized void startToFeedPersistantBuffer() {
        try {
            scheduler.resumeTrigger(cronFeeder.getKey());
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public synchronized void stopToFeedPersistentBuffer() {
        try {
            scheduler.pauseTrigger(cronFeeder.getKey());
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public synchronized void startToSwapFromPersistentToVolatileMemory() {
        try {
            scheduler.resumeTrigger(cronSwapper.getKey());
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public void stopToSwapFromPersistentToVolatileMemory() {
        try {
            scheduler.pauseTrigger(cronSwapper.getKey());
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public void setFeedCronExpr(String feedCronExpr) {
        try {
            scheduler.deleteJob(jobFeeder.getKey());
            cronFeeder = cronFeeder.getTriggerBuilder().withSchedule(CronScheduleBuilder.cronSchedule(feedCronExpr)).build();
            scheduler.scheduleJob(jobFeeder, cronFeeder);
        } catch ( SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public void setSwapCronExpr(String swapCronExpr) {
        try {
            scheduler.deleteJob(jobSwapper.getKey());
            cronSwapper = cronSwapper.getTriggerBuilder().withSchedule(CronScheduleBuilder.cronSchedule(swapCronExpr)).build();
            scheduler.scheduleJob(jobSwapper, cronSwapper);
        } catch ( SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public String getFeedCronExpr() {
        return cronFeeder.getCronExpression();
    }

    public String getSwapCronExpr() {
        return cronSwapper.getCronExpression();
    }

    public void pause() {
        try {
            scheduler.standby();
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public void resume() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public void shutdown() {
        try {
            scheduler.shutdown(true);
            while (!scheduler.isShutdown()) {
                // wait
            }
            container.getPersistentBuffer().dispose();
        } catch (SchedulerException e) {
            throw new CaptchaException(e);
        }
    }

    public int getPersistentFeedings() {
        return container.getPersistentFeedings().intValue();
    }

    public int getPersistentMemoryHits() {
        return container.getPersistentMemoryHits().intValue();
    }

    public int getPersistentToVolatileSwaps() {
        return container.getPersistentToVolatileSwaps().intValue();
    }

    public int getVolatileMemoryHits() {
        return container.getVolatileMemoryHits().intValue();
    }

    public int getFeedSize() {
        return config.getFeedSize().intValue();
    }

    public void setFeedSize(int feedSize) {
        config.setFeedSize(feedSize);
    }

    public HashedMap getLocaleRatio() {
        return config.getLocaleRatio();
    }

    public synchronized void setLocaleRatio(String localeName, double ratio) {
        Locale locale = getLocaleFromName(localeName);
        MapIterator it = config.getLocaleRatio().mapIterator();
        boolean isSet = false;
        double coef = ratio;
        double oldValue = 0.0;

        if (config.getLocaleRatio().containsKey(locale)) {
            oldValue = ((Double) config.getLocaleRatio().get(locale));
            coef = ratio - oldValue;
        }

        while (it.hasNext()) {
            Locale tempLocale = (Locale) it.next();
            double value = ((Double) it.getValue());
            if (locale.equals(tempLocale)) {
                it.setValue(coef + value);
                isSet = true;
            } else {
                it.setValue(value - (value * coef));
            }
        }

        if (!isSet) {
            config.getLocaleRatio().put(locale, ratio);
        }
    }

    protected Locale getLocaleFromName(String localeName) {
        StringTokenizer tokenizer = new StringTokenizer(localeName, "_");
        switch (tokenizer.countTokens()) {
            case 2:
                return new Locale(tokenizer.nextToken(), tokenizer.nextToken());
            case 3:
                return new Locale(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
            default:
                return Locale.getDefault();
        }
    }

    public synchronized void removeLocaleRatio(String localeName) {
        Locale locale = getLocaleFromName(localeName);
        if (config.getLocaleRatio().containsKey(locale)) {
            setLocaleRatio(localeName, 0.0);
            config.getLocaleRatio().remove(locale);
        }
    }

    public int getMaxPersistentMemorySize() {
        return config.getMaxPersistentMemorySize();
    }

    public void setMaxPersistentMemorySize(int size) {
        config.setMaxPersistentMemorySize(size);
    }

    public int getMaxVolatileMemorySize() {
        return config.getMaxVolatileMemorySize();
    }

    public void setMaxVolatileMemorySize(int size) {
        config.setMaxVolatileMemorySize(size);
    }

    public int getSwapSize() {
        return config.getSwapSize();
    }

    public void setSwapSize(int swapSize) {
        config.setSwapSize(swapSize);
    }

    public int getVolatileBufferSize() {
        return container.getVolatileBuffer().size();
    }

    public HashedMap getVolatileBufferSizeByLocales() {
        HashedMap map = new HashedMap();
        for (Locale locale : (Iterable<Locale>) container.getVolatileBuffer().getLocales()) {
            map.put(locale, container.getVolatileBuffer().size(locale));
        }
        return map;
    }

    public int getPersistentBufferSize() {
        return container.getPersistentBuffer().size();
    }

    public HashedMap getPersistentBufferSizesByLocales() {
        HashedMap map = new HashedMap();
        for (Locale locale : (Iterable<Locale>) container.getPersistentBuffer().getLocales()) {
            map.put(locale, container.getPersistentBuffer().size(locale));
        }
        return map;
    }

    public void clearVolatileBuffer() {
        container.getVolatileBuffer().clear();
    }

    public void clearPersistentBuffer() {
        container.getPersistentBuffer().clear();
    }
}
