package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVHardSkillsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVHardSkills getTechCVHardSkillsSample1() {
        return new TechCVHardSkills().id(1L).skills("skills1");
    }

    public static TechCVHardSkills getTechCVHardSkillsSample2() {
        return new TechCVHardSkills().id(2L).skills("skills2");
    }

    public static TechCVHardSkills getTechCVHardSkillsRandomSampleGenerator() {
        return new TechCVHardSkills().id(longCount.incrementAndGet()).skills(UUID.randomUUID().toString());
    }
}
