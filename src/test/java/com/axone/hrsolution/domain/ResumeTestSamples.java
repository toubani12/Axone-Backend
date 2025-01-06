package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ResumeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Resume getResumeSample1() {
        return new Resume().id(1L);
    }

    public static Resume getResumeSample2() {
        return new Resume().id(2L);
    }

    public static Resume getResumeRandomSampleGenerator() {
        return new Resume().id(longCount.incrementAndGet());
    }
}
