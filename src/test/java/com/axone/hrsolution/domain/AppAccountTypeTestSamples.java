package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppAccountTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppAccountType getAppAccountTypeSample1() {
        return new AppAccountType().id(1L).type("type1");
    }

    public static AppAccountType getAppAccountTypeSample2() {
        return new AppAccountType().id(2L).type("type2");
    }

    public static AppAccountType getAppAccountTypeRandomSampleGenerator() {
        return new AppAccountType().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString());
    }
}
