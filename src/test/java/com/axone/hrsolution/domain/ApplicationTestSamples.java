package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Application getApplicationSample1() {
        return new Application().id(1L).title("title1").description("description1").numberOfCandidates(1);
    }

    public static Application getApplicationSample2() {
        return new Application().id(2L).title("title2").description("description2").numberOfCandidates(2);
    }

    public static Application getApplicationRandomSampleGenerator() {
        return new Application()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .numberOfCandidates(intCount.incrementAndGet());
    }
}
