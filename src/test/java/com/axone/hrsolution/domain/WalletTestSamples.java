package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class WalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Wallet getWalletSample1() {
        return new Wallet().id(1L);
    }

    public static Wallet getWalletSample2() {
        return new Wallet().id(2L);
    }

    public static Wallet getWalletRandomSampleGenerator() {
        return new Wallet().id(longCount.incrementAndGet());
    }
}
