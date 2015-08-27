/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.TestExecutions;
import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.stages.Stage;
import com.calamp.connect.messageprocessor.exceptions.ExpectedTestException;

public class DummyStageExceptor extends Stage{

    public DummyStageExceptor(String ident) {
        super(ident);
    }

    @Override
    public <I,O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) throws ExpectedTestException {
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
        // The first element of the future becomes the present.
        outboundPayload.advance(this.getStageIdentifer());
        TestExecutions.basicOperation(this, inPayload, outboundPayload);
        //Throw Exception.
        throw new ExpectedTestException();
    }

}
