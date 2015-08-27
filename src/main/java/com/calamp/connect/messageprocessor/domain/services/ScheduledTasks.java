package com.calamp.connect.messageprocessor.domain.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.Util;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.testing.environment.TestEnv;

@Service
public class ScheduledTasks {

    private static final Logger log = Logger.getLogger(ScheduledTasks.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired(required = true)
    SQSerializeDeserializeService sds;

    @Autowired(required = true)
    JmSqsMessageProducer provider;

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
        // this.customPullFromSQS();
        // jmsPullFromSQS();
    }

    /**
     * JMS based polling solution for reading messages from SQS.
     */
    private <E> void jmsPullFromSQS() {
        try {
            consumer.readMessage();
        } catch (JMSException e) {
            log.error(e);
        }
    }

    /**
     * Custom polling solution for reading SQS.
     */
    private <E> void customPullFromSQS() {
        String mtxt = null;
        while ((mtxt = eventMessageOut.recieveMessage()) != null) {
            log.info("Message String: " + mtxt);
            ProcessingWrapper<E> payload;
            payload = Util.wrapData(this.sds.deserializeFromSqs(mtxt), pathService.initializePath(mtxt));
            // This returns a Java Future, it can be waited on, put in a list
            Future<ProcessingWrapper<E>> ret = stageService.processMessage(payload);
        }
    }

    // Push based JMS-SQS solution.
    @JmsListener(destination = "na-eventMessageOut-1", containerFactory = "JmSqsContainerFactory")
    public <E> void receiveMessage(String message) {
        log.info("Received Message String <" + message + ">");
        E reconstructed = sds.deserializeFromSqs(message);
        log.info("Reconstruct Message String <" + reconstructed.getClass() + ">: " + reconstructed.toString());
        ProcessingWrapper<E> payload;
        payload = Util.wrapData(reconstructed, pathService.initializePath(reconstructed));
        Future<ProcessingWrapper<E>> ret = stageService.processMessage(payload);
        if (Constants.debug) {
            TestEnv.pushResponse(ret);
        }
    }

}