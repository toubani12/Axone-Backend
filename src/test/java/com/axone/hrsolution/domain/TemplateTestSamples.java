package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Template getTemplateSample1() {
        return new Template().id(1L).label("label1").description("description1");
    }

    public static Template getTemplateSample2() {
        return new Template().id(2L).label("label2").description("description2");
    }

    public static Template getTemplateRandomSampleGenerator() {
        return new Template().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
