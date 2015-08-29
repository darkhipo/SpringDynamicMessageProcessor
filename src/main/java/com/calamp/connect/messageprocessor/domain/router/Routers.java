/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.router;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;
import com.calamp.connect.messageprocessor.domain.transformers.Transforms;

@Component
public class Routers {

    private static Logger log = Logger.getLogger(Routers.class.getName());

    @Router(inputChannel = Constants.sourceChannelName, defaultOutputChannel = Constants.targetChannelName, applySequence = "true")
    public <E> List<String> route(Message<ProcessingWrapper<E>> inbound) throws InterruptedException, InvalidAlgorithmParameterException {
        String nextHop = null;
        if (inbound.getHeaders().containsKey(Constants.nextHopHeaderName)) {
            nextHop = String.valueOf(inbound.getHeaders().get(Constants.nextHopHeaderName));
        } else {
            nextHop = inbound.getPayload().nextStepPeek();
        }

        log.info("Route [" + "Payload: " + inbound + " Next-Hop: " + nextHop + "]");
        List<String> routesTo = new ArrayList<String>();
        Stage currentStage = Transforms.resolveStage(nextHop);

        if (currentStage == null || currentStage.getStageIdentifer().equals(Constants.terminalStageTag)) {
            routesTo.add(Constants.targetChannelName);
        } else {
            routesTo.add(Constants.stageChannelName);
        }

        return routesTo;
    }

}
