/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.services;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

@Service
@MessageEndpoint
public class ErrorHandlerService<E> {

    private static final Logger log = Logger.getLogger(ErrorHandlerService.class.getName());

    @ServiceActivator(inputChannel = Constants.errorChannelName, outputChannel = Constants.targetChannelName)
    public Message<ProcessingWrapper<E>> handleErrorMessage(Message<Exception> errorMessage) {
        Exception e = errorMessage.getPayload();
        String preamble = "[Error]: ";
        log.info(preamble + e);
        ProcessingWrapper<E> errorWrapper = new ProcessingWrapper<E>(e);
        Message<ProcessingWrapper<E>> m1;
        m1 = MessageBuilder.withPayload(errorWrapper).copyHeaders(errorMessage.getHeaders()).build();
        return m1;
    }

}