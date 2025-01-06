package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class NDATestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NDA getNDASample1() {
        return new NDA().id(1L);
    }

    public static NDA getNDASample2() {
        return new NDA().id(2L);
    }

    public static NDA getNDARandomSampleGenerator() {
        return new NDA().id(longCount.incrementAndGet());
    }
}
