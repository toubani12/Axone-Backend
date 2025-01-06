package com.axone.hrsolution.service;

import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProcessService {

    @Autowired
    private RuntimeService runtimeService;

    public void startProcess() {
        runtimeService.startProcessInstanceByKey("CREATE_ACCOUNT");
    }
}
