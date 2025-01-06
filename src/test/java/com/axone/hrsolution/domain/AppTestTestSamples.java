package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppTest getAppTestSample1() {
        return new AppTest()
            .id(1L)
            .name("name1")
            .invitationLink("invitationLink1")
            .totalScore(1)
            .status("status1")
            .testID(1L)
            .algorithm("algorithm1")
            .duration(1);
    }

    public static AppTest getAppTestSample2() {
        return new AppTest()
            .id(2L)
            .name("name2")
            .invitationLink("invitationLink2")
            .totalScore(2)
            .status("status2")
            .testID(2L)
            .algorithm("algorithm2")
            .duration(2);
    }

    public static AppTest getAppTestRandomSampleGenerator() {
        return new AppTest()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .invitationLink(UUID.randomUUID().toString())
            .totalScore(intCount.incrementAndGet())
            .status(UUID.randomUUID().toString())
            .testID(longCount.incrementAndGet())
            .algorithm(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet());
    }
}
