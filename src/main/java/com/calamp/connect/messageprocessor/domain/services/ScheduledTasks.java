package com.calamp.connect.messageprocessor.domain.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.annotation.JmsListener;
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
    JmSqsMessageProvider provider;

    @Autowired(required = true)
    JmSqsMessageConsumer consumer;

    @Autowired(required = true)
    private SQSConnectionService eventMessageOut;

    @Autowired(required = true)
    private RouteAndProcessService stageService;

    @Autowired(required = true)
    private PathInitializationService pathService;

    @Scheduled(fixedRate = Constants.sqsPollDelayMillis)
    public void pollAndProcess() {

        log.info("pollAndProcess the time is now " + dateFormat.format(new Date()));

        if(Constants.debug){
            provider.sendMessage("This is a test");
        }

        /*
         * //Custom polling solution for reading SQS. String mtxt = null; while
         * ((mtxt = eventMessageOut.recieveMessage()) != null) {
         * log.info("Message String: " + mtxt); ProcessingWrapper<String>
         * payload = Util.wrapData(Util.deserializeFromSqs(mtxt),
         * pathService.initializePath(mtxt)); // This returns a Java Future, it
         * can be waited on, put in a list stageService.processMessage(payload);
         * }
         */

        /*
         * // JMS based polling solution for reading messages from SQS. try {
         * consumer.readMessage(); } catch (JMSException e) { log.error(e); }
         */
    }

    // Push based JMS-SQS solution.
    @JmsListener(destination = "na-eventMessageOut-1", containerFactory = "JmSqsContainerFactory")
    public void receiveMessage(String message) {
        log.info("Received Message String <" + message + ">");
        pathService.initializePath(message);
    }

}