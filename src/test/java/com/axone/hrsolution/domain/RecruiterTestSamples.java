package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RecruiterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Recruiter getRecruiterSample1() {
        return new Recruiter()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .address("address1")
            .name("name1")
            .label("label1")
            .linkedinUrl("linkedinUrl1");
    }

    public static Recruiter getRecruiterSample2() {
        return new Recruiter()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .address("address2")
            .name("name2")
            .label("label2")
            .linkedinUrl("linkedinUrl2");
    }

    public static Recruiter getRecruiterRandomSampleGenerator() {
        return new Recruiter()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .label(UUID.randomUUID().toString())
            .linkedinUrl(UUID.randomUUID().toString());
    }
}
