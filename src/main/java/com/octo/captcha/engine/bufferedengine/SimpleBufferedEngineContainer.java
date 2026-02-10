package com.octo.captcha.engine.bufferedengine;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple implementation of the BufferedEngineContainer using ScheduledExecutorService.
 *
 * @author Benoit Doumas
 * @author Marc-Antoine Garrigue
 */
public class SimpleBufferedEngineContainer extends BufferedEngineContainer {

    private static final Log log = LogFactory.getLog(SimpleBufferedEngineContainer.class.getName());

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private long feedPeriod = 10000; // 10 seconds
    private long swapPeriod = 1000;  // 1 second

    public SimpleBufferedEngineContainer(CaptchaEngine engine, CaptchaBuffer memoryBuffer,
                                         CaptchaBuffer diskBuffer, ContainerConfiguration containerConfiguration,
                                         int feedPeriod, int swapPeriod) {
        super(engine, memoryBuffer, diskBuffer, containerConfiguration);
        this.feedPeriod = feedPeriod;
        this.swapPeriod = swapPeriod;
        startScheduler();
    }

    /**
     * Starts the scheduler for feeding and swapping tasks.
     */
    //@Override
    protected void startScheduler() {
        log.debug("Starting feed and swap schedulers...");
        scheduler.scheduleAtFixedRate(new SimpleDiskFeeder(), 0, feedPeriod, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(new SimpleDiskToMemory(), 0, swapPeriod, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the scheduler.
     */
    protected void stopDaemon() {
        log.debug("Shutting down scheduler...");
        scheduler.shutdownNow();
    }

    private class SimpleDiskFeeder implements Runnable {
        @Override
        public void run() {
            try {
                feedPersistentBuffer();
            } catch (Exception e) {
                log.error("Error feeding persistent buffer", e);
            }
        }
    }

    private class SimpleDiskToMemory implements Runnable {
        @Override
        public void run() {
            try {
                swapCaptchasFromPersistentToVolatileMemory();
            } catch (Exception e) {
                log.error("Error swapping captchas to volatile memory", e);
            }
        }
    }
}
