package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechnicalCVTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechnicalCV getTechnicalCVSample1() {
        return new TechnicalCV().id(1L).name("name1").profileDescription("profileDescription1");
    }

    public static TechnicalCV getTechnicalCVSample2() {
        return new TechnicalCV().id(2L).name("name2").profileDescription("profileDescription2");
    }

    public static TechnicalCV getTechnicalCVRandomSampleGenerator() {
        return new TechnicalCV()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .profileDescription(UUID.randomUUID().toString());
    }
}
