package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InterviewTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Interview getInterviewSample1() {
        return new Interview().id(1L).label("label1").entryLink("entryLink1").invitationLink("invitationLink1").comments("comments1");
    }

    public static Interview getInterviewSample2() {
        return new Interview().id(2L).label("label2").entryLink("entryLink2").invitationLink("invitationLink2").comments("comments2");
    }

    public static Interview getInterviewRandomSampleGenerator() {
        return new Interview()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .entryLink(UUID.randomUUID().toString())
            .invitationLink(UUID.randomUUID().toString())
            .comments(UUID.randomUUID().toString());
    }
}
