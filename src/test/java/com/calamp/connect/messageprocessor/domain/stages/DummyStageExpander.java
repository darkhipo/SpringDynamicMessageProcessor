/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.TestExecutions;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;

public class DummyStageExpander extends Stage {

    private DummyStage expandedHop;

    public DummyStageExpander(String ident, DummyStage expandTo) {
        super(ident);
        this.expandedHop = expandTo;
    }

    @Override
    public <I, O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) {
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
        // The first element of the future becomes the present.
        outboundPayload.advance(this.getStageIdentifer());
        TestExecutions.basicOperation(this, inPayload, outboundPayload);
        // Add nexthop as expanding stage.
        outboundPayload.getFuturePath().add(0, this.expandedHop.getStageIdentifer());

        return outboundPayload;
    }

}
