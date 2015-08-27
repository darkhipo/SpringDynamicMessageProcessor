/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.TestExecutions;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;

public class DummyStage extends Stage {

    public DummyStage(String ident) {
        super(ident);
    }

    @Override
    public <I, O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) {
        // The first element of the future becomes the present.
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
        outboundPayload.advance(this.getStageIdentifer());
        TestExecutions.basicOperation(this, inPayload, outboundPayload);
        // Modify outbound payload data here.
        return outboundPayload;
    }

}
