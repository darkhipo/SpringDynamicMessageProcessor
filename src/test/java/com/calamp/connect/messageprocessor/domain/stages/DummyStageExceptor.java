package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExceptionPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;
import com.calamp.connect.messageprocessor.domain.stages.Stage;
import com.calamp.connect.messageprocessor.testing.exceptions.ExpectedTestException;

public class DummyStageExceptor extends Stage{

    public DummyStageExceptor(String ident) {
        super(ident);
    }

    @Override
    public <I,O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) throws ExpectedTestException {
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
        // The first element of the future becomes the present.
        outboundPayload.advance(this.getStageIdentifer());

        String dataAsString = null;
        if (inPayload.getDataPayload() instanceof StaticPathClass ) {
            StaticPathClass spc = (StaticPathClass) inPayload.getDataPayload();
            dataAsString = spc.getPayload() + " " + this.getStageIdentifer();
            spc.setPayload(dataAsString);
            outboundPayload.setDataPayload( (O) spc );
        }
        else if (inPayload.getDataPayload() instanceof ExpandingPathClass ){
            ExpandingPathClass epc = (ExpandingPathClass) inPayload.getDataPayload();
            dataAsString = epc.getPayload() + " " + this.getStageIdentifer();
            epc.setPayload(dataAsString);
            outboundPayload.setDataPayload( (O) epc );
        }
        else if (inPayload.getDataPayload() instanceof ClearFuturePathClass) {
            ClearFuturePathClass epc = (ClearFuturePathClass) inPayload.getDataPayload();
            dataAsString = epc.getPayload() + " " + this.getStageIdentifer();
            epc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) epc);
        }
        else if (inPayload.getDataPayload() instanceof ExceptionPathClass) {
            ExceptionPathClass expc = (ExceptionPathClass) inPayload.getDataPayload();
            dataAsString = expc.getPayload() + " " + this.getStageIdentifer();
            expc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) expc);
        }
        //Throw Exception.
        throw new ExpectedTestException();
    }

}
