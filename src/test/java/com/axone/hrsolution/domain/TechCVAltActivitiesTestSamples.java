package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVAltActivitiesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVAltActivities getTechCVAltActivitiesSample1() {
        return new TechCVAltActivities().id(1L).activities("activities1");
    }

    public static TechCVAltActivities getTechCVAltActivitiesSample2() {
        return new TechCVAltActivities().id(2L).activities("activities2");
    }

    public static TechCVAltActivities getTechCVAltActivitiesRandomSampleGenerator() {
        return new TechCVAltActivities().id(longCount.incrementAndGet()).activities(UUID.randomUUID().toString());
    }
}
