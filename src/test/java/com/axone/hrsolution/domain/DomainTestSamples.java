package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DomainTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Domain getDomainSample1() {
        return new Domain().id(1L).name("name1");
    }

    public static Domain getDomainSample2() {
        return new Domain().id(2L).name("name2");
    }

    public static Domain getDomainRandomSampleGenerator() {
        return new Domain().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
