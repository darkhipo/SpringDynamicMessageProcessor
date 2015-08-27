/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

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

@Service
public class ScheduledTasks {

    private static final Logger log = Logger.getLogger(ScheduledTasks.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired(required = true)
    SerializeDeserializeService sds;

    @Autowired(required = true)
    JmsSqsMessageProducer provider;

    @Autowired(required = true)
    JmsSqsMessageConsumer consumer;

    @Autowired(required = true)
    private RouteAndProcessService stageService;

    @Autowired(required = true)
    private PathInitializationServiceInterface pathService;

    @Autowired(required = true)
    private SQSConnectionServiceInterface sqsConnService;

    @Autowired
    private ReplyProcessServiceInterface rpsi;

    @Scheduled(fixedRate = Constants.sqsPollDelayMillis)
    public void pollAndProcess() {
        log.info("pollAndProcess the time is now " + dateFormat.format(new Date()));
        // this.customPullFromSQS();
        // jmsPullFromSQS();
    }

    /**
     * Push based JMS-SQS solution.
     * 
     * @param The
     *            message received.
     */
    @JmsListener(destination = "na-eventMessageOut-1", containerFactory = "JmSqsContainerFactory")
    public <E> void receiveMessage(String message) {
        log.info("Received Message String <" + message + ">");
        E reconstructed = this.sds.stringToObject(message);
        log.info("Reconstruct Message String <" + reconstructed.getClass() + ">: " + reconstructed.toString());
        ProcessingWrapper<E> payload;
        payload = Util.wrapData(reconstructed, pathService.initializePath(reconstructed));
        Future<ProcessingWrapper<E>> ret = stageService.processMessage(payload);
        this.rpsi.pushResponse(ret);
    }

    /**
     * JMS based polling solution for reading messages from SQS.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    private <E> void customPullFromSQS() {
        String mtxt = null;
        while ((mtxt = sqsConnService.recieveMessage()) != null) {
            log.info("Message String: " + mtxt);
            ProcessingWrapper<E> payload;
            payload = Util.wrapData(this.sds.stringToObject(mtxt), pathService.initializePath(mtxt));
            // This returns a Java Future, it can be waited on, put in a list
            Future<ProcessingWrapper<E>> ret = stageService.processMessage(payload);
        }
    }

}