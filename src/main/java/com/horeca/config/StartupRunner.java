package com.horeca.config;

import org.apache.log4j.Logger;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

// responsible for performing various maintenance actions after
// the startup i.e. after the application context has been loaded
@Service
public class StartupRunner {

    private static final Logger logger = Logger.getLogger(StartupRunner.class);

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        logger.info("STARTED...");
    }
}
