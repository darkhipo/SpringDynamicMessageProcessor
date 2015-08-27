package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExceptionPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;

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
        String dataAsString = null;
        if (inPayload.getDataPayload() instanceof StaticPathClass) {
            StaticPathClass spc = (StaticPathClass) inPayload.getDataPayload();
            dataAsString = spc.getPayload() + " " + this.getStageIdentifer();
            spc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) spc);
        } else if (inPayload.getDataPayload() instanceof ExpandingPathClass) {
            ExpandingPathClass epc = (ExpandingPathClass) inPayload.getDataPayload();
            dataAsString = epc.getPayload() + " " + this.getStageIdentifer();
            epc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) epc);
        } else if (inPayload.getDataPayload() instanceof ClearFuturePathClass) {
            ClearFuturePathClass cfpc = (ClearFuturePathClass) inPayload.getDataPayload();
            dataAsString = cfpc.getPayload() + " " + this.getStageIdentifer();
            cfpc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) cfpc);
        } else if (inPayload.getDataPayload() instanceof ExceptionPathClass) {
            ExceptionPathClass expc = (ExceptionPathClass) inPayload.getDataPayload();
            dataAsString = expc.getPayload() + " " + this.getStageIdentifer();
            expc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) expc);
        }
        // Clear future stages.
        outboundPayload.getFuturePath().clear();

        return outboundPayload;
    }

}
