package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TechCVDocsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TechCVDocs getTechCVDocsSample1() {
        return new TechCVDocs().id(1L);
    }

    public static TechCVDocs getTechCVDocsSample2() {
        return new TechCVDocs().id(2L);
    }

    public static TechCVDocs getTechCVDocsRandomSampleGenerator() {
        return new TechCVDocs().id(longCount.incrementAndGet());
    }
}
