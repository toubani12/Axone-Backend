package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppAccountTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppAccount getAppAccountSample1() {
        return new AppAccount().id(1L).accountNumber(1L).cardNumber(1L).cvv(1);
    }

    public static AppAccount getAppAccountSample2() {
        return new AppAccount().id(2L).accountNumber(2L).cardNumber(2L).cvv(2);
    }

    public static AppAccount getAppAccountRandomSampleGenerator() {
        return new AppAccount()
            .id(longCount.incrementAndGet())
            .accountNumber(longCount.incrementAndGet())
            .cardNumber(longCount.incrementAndGet())
            .cvv(intCount.incrementAndGet());
    }
}
