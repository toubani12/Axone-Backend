package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppTestTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppTestType getAppTestTypeSample1() {
        return new AppTestType().id(1L).type("type1");
    }

    public static AppTestType getAppTestTypeSample2() {
        return new AppTestType().id(2L).type("type2");
    }

    public static AppTestType getAppTestTypeRandomSampleGenerator() {
        return new AppTestType().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString());
    }
}
