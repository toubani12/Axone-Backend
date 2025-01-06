package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdminTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Admin getAdminSample1() {
        return new Admin().id(1L).systemName("systemName1");
    }

    public static Admin getAdminSample2() {
        return new Admin().id(2L).systemName("systemName2");
    }

    public static Admin getAdminRandomSampleGenerator() {
        return new Admin().id(longCount.incrementAndGet()).systemName(UUID.randomUUID().toString());
    }
}
