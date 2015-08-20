package com.calamp.connect.messageprocessor.domain.services;

import java.util.concurrent.Future;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

@MessagingGateway(defaultRequestChannel = Constants.sourceChannelName, defaultReplyChannel = Constants.targetChannelName, errorChannel = Constants.errorChannelName, defaultRequestTimeout = Constants.requestTimeoutMillis, defaultReplyTimeout = Constants.replyTimeoutMillis)
public interface RouteAndProcessService {

    @Gateway(requestChannel = Constants.sourceChannelName, replyChannel = Constants.targetChannelName) <E> Future<ProcessingWrapper <E>> processMessage(ProcessingWrapper<E> message, @Header(Constants.nextHopHeaderName) String nextHop);
    @Gateway(requestChannel = Constants.sourceChannelName, replyChannel = Constants.targetChannelName) <E> Future<ProcessingWrapper <E>> processMessage(ProcessingWrapper <E> message);
}
