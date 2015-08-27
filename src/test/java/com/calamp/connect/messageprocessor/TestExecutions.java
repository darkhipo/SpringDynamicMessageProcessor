/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor;

import com.calamp.connect.messageprocessor.domain.model.CapsuleInterface;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;

public class TestExecutions {

    public static <I, O> ProcessingWrapper<O> basicOperation(Stage stage, ProcessingWrapper<I> inPayload, ProcessingWrapper<O> outboundPayload) {
        // I must assume that the inbound payloads are of type CapsuleInterface<String> to make this dangerous cast. 
        // This is done so that different stages can share common opperations (through the interface).
        CapsuleInterface<String> ci = (CapsuleInterface<String>) inPayload.getDataPayload();
        // String payloads used for testing. All default actions just append stage identifiers to the core string payload.
        String dataAsString = ci.getPayload() + " " + stage.getStageIdentifer();
        ci.setPayload(dataAsString);
        // This cast isn't dangerous; if the first cast worked then we are assured that this subsequent cast will work.
        outboundPayload.setDataPayload( (O) ci );
        return outboundPayload;
    }
}
