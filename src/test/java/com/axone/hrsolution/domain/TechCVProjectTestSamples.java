package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVProject getTechCVProjectSample1() {
        return new TechCVProject().id(1L).project("project1");
    }

    public static TechCVProject getTechCVProjectSample2() {
        return new TechCVProject().id(2L).project("project2");
    }

    public static TechCVProject getTechCVProjectRandomSampleGenerator() {
        return new TechCVProject().id(longCount.incrementAndGet()).project(UUID.randomUUID().toString());
    }
}
