package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flowable")
public class ProcessController {

    private static final Logger log = LoggerFactory.getLogger(ProcessController.class);
    @Autowired
    private ProcessService processService;

    @GetMapping("/create-account")
    public String startProcess() {
        processService.startProcess();
        log.info("account creation started");
        return "Process started";
    }
}
