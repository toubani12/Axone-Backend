package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProviderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Provider getProviderSample1() {
        return new Provider().id(1L).name("name1").country("country1");
    }

    public static Provider getProviderSample2() {
        return new Provider().id(2L).name("name2").country("country2");
    }

    public static Provider getProviderRandomSampleGenerator() {
        return new Provider().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).country(UUID.randomUUID().toString());
    }
}
