package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVEducationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVEducation getTechCVEducationSample1() {
        return new TechCVEducation().id(1L).education("education1");
    }

    public static TechCVEducation getTechCVEducationSample2() {
        return new TechCVEducation().id(2L).education("education2");
    }

    public static TechCVEducation getTechCVEducationRandomSampleGenerator() {
        return new TechCVEducation().id(longCount.incrementAndGet()).education(UUID.randomUUID().toString());
    }
}
