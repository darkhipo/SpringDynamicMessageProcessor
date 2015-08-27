/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.transformers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;

@Component
public class Transforms {

    private static Logger log = Logger.getLogger(Transforms.class.getName());
    private static Map<String, Stage> stageMap;

    @PostConstruct
    private void setup () {
        Transforms.stageMap = new HashMap<String, Stage>();
        Transforms.stageMap.put(Constants.terminalStageTag, null);
    }

    public static void registerStage(Stage newStage) {
        log.info("Register Stage " + newStage.getStageIdentifer());
        if (!newStage.getStageIdentifer().equals(Constants.terminalStageTag)) {
            Transforms.stageMap.put(newStage.getStageIdentifer(), newStage);
        }
    }

    public static Set<String> getStageKeys() {
        return Transforms.stageMap.keySet();
    }

    public static final Stage resolveStage(String key) {
        log.info("Resolve Stage " + key);
        if (Transforms.stageMap.containsKey(key)) {
            return Transforms.stageMap.get(key);
        }
        return null;
    }

    private static <E> Message<ProcessingWrapper <E>> enrichWithHeader(Message<ProcessingWrapper<E>> messageIn) {
        ProcessingWrapper<E> outboundPayload = messageIn.getPayload();
        String nextHop = outboundPayload.nextStepPeek();
        Message<ProcessingWrapper<E>> m1;
        m1 = MessageBuilder.withPayload(outboundPayload).copyHeaders(messageIn.getHeaders()).setHeader(Constants.nextHopHeaderName, nextHop).build();
        return m1;
    }

    @Transformer(inputChannel = Constants.stageChannelName, outputChannel = Constants.sourceChannelName)
    public static <E> Message<ProcessingWrapper <E>> transform(Message<ProcessingWrapper <E>> messageIn) throws Exception {
        ProcessingWrapper<E> inboundPayload = messageIn.getPayload();
        if (!messageIn.getHeaders().containsKey(Constants.nextHopHeaderName)) {
            messageIn = enrichWithHeader(messageIn);
        }

        Stage stage = Transforms.resolveStage(inboundPayload.nextStepPeek());
        ProcessingWrapper<E> outboundPayload = stage.enact(inboundPayload);
        String nextHop = outboundPayload.nextStepPeek();

        if ( nextHop == null || stage == null ) {
            nextHop = Constants.terminalStageTag;
        }

        String logstr = "Transform " + inboundPayload.getSiIdent() + " AT: ";
        logstr += inboundPayload.nextStepPeek() + " NEXT-HOP: " + nextHop;
        log.info(logstr);

        Message<ProcessingWrapper<E>> m1;
        m1 = MessageBuilder.withPayload(outboundPayload).copyHeaders(messageIn.getHeaders()).setHeader(Constants.nextHopHeaderName, nextHop).build();

        return m1;
    }

}