package com.harmonicinc.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

public class TestCLI implements CommandLineRunner {
    private static Logger LOG = LoggerFactory.getLogger(TestCLI.class);

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Starting TEST application");
    }
}
