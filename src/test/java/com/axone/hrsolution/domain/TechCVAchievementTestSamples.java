package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVAchievementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVAchievement getTechCVAchievementSample1() {
        return new TechCVAchievement().id(1L).achievement("achievement1");
    }

    public static TechCVAchievement getTechCVAchievementSample2() {
        return new TechCVAchievement().id(2L).achievement("achievement2");
    }

    public static TechCVAchievement getTechCVAchievementRandomSampleGenerator() {
        return new TechCVAchievement().id(longCount.incrementAndGet()).achievement(UUID.randomUUID().toString());
    }
}
