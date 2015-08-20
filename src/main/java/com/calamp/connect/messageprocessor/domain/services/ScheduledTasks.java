package com.calamp.connect.messageprocessor.domain.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.Util;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

@Service
public class ScheduledTasks {

    private static final Logger log = Logger.getLogger(ScheduledTasks.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired(required = true)
    private SQSConnectionService eventMessageOut;

    @Autowired(required = true)
    private RouteAndProcessService stageService;
    
    @Autowired(required = true)
    private PathInitializationService pathService;

    @Scheduled(fixedRate = Constants.sqsPollDelayMillis)
    public void pollAndProcess() {
        log.info("pollAndProcess the time is now " + dateFormat.format(new Date()));

        String mtxt = null;
        while ((mtxt = eventMessageOut.recieveMessage()) != null) {
            log.info("Message String: " + mtxt);
            ProcessingWrapper<String> payload = Util.wrapData( Util.deserializeFromSqs(mtxt), pathService.initializePath(mtxt));
            //This returns a Java Future, it can be waited on, put in a list etc.
            stageService.processMessage(payload);
        }
    }
}