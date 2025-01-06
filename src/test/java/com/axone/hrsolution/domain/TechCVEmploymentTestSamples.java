package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVEmploymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVEmployment getTechCVEmploymentSample1() {
        return new TechCVEmployment().id(1L).employment("employment1");
    }

    public static TechCVEmployment getTechCVEmploymentSample2() {
        return new TechCVEmployment().id(2L).employment("employment2");
    }

    public static TechCVEmployment getTechCVEmploymentRandomSampleGenerator() {
        return new TechCVEmployment().id(longCount.incrementAndGet()).employment(UUID.randomUUID().toString());
    }
}
