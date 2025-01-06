package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVSoftSkillsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVSoftSkills getTechCVSoftSkillsSample1() {
        return new TechCVSoftSkills().id(1L).skills("skills1");
    }

    public static TechCVSoftSkills getTechCVSoftSkillsSample2() {
        return new TechCVSoftSkills().id(2L).skills("skills2");
    }

    public static TechCVSoftSkills getTechCVSoftSkillsRandomSampleGenerator() {
        return new TechCVSoftSkills().id(longCount.incrementAndGet()).skills(UUID.randomUUID().toString());
    }
}
