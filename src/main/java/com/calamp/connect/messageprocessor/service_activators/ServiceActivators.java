package com.calamp.connect.messageprocessor.service_activators;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.calamp.connect.messageprocessor.Constants;

@Component
@MessageEndpoint
public class ServiceActivators {

    private static final Logger log = Logger.getLogger(ServiceActivators.class.getName());

    @ServiceActivator(inputChannel = Constants.targetChannelName)
    public void printObject(Object obj) {
        String preamble = "[Message at Completion]: ";
        log.info(preamble + obj);
    }
}
