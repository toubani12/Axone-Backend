package com.axone.hrsolution.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContractTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContractType getContractTypeSample1() {
        return new ContractType().id(1L).name("name1");
    }

    public static ContractType getContractTypeSample2() {
        return new ContractType().id(2L).name("name2");
    }

    public static ContractType getContractTypeRandomSampleGenerator() {
        return new ContractType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
