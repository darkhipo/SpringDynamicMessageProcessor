/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.TestExecutions;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

public class DummyStageClearer extends Stage {

    public DummyStageClearer(String ident) {
        super(ident);
    }

    @Override
    public <I, O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) {
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
        // The first element of the future becomes the present.
        outboundPayload.advance(this.getStageIdentifer());
        // String payloads used for testing.
        TestExecutions.basicOperation(this, inPayload, outboundPayload);
        // Clear future stages.
        outboundPayload.getFuturePath().clear();

        return outboundPayload;
    }

}
