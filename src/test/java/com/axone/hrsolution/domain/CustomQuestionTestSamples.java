package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomQuestion getCustomQuestionSample1() {
        return new CustomQuestion().id(1L).question("question1").answer("answer1").correctAnswer("correctAnswer1");
    }

    public static CustomQuestion getCustomQuestionSample2() {
        return new CustomQuestion().id(2L).question("question2").answer("answer2").correctAnswer("correctAnswer2");
    }

    public static CustomQuestion getCustomQuestionRandomSampleGenerator() {
        return new CustomQuestion()
            .id(longCount.incrementAndGet())
            .question(UUID.randomUUID().toString())
            .answer(UUID.randomUUID().toString())
            .correctAnswer(UUID.randomUUID().toString());
    }
}
