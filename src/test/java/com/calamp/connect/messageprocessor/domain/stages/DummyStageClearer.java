package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;

public class DummyStageClearer extends Stage{

    public DummyStageClearer(String ident) {
        super(ident);
    }

    @Override
    public <I, O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) {
        // The first element of the future becomes the present.
        ProcessingWrapper<O> outboundPayload = new ProcessingWrapper<O>(inPayload);
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
        }
        else if (inPayload.getDataPayload() instanceof ClearFuturePathClass) {
            ClearFuturePathClass epc = (ClearFuturePathClass) inPayload.getDataPayload();
            dataAsString = epc.getPayload() + " " + this.getStageIdentifer();
            epc.setPayload(dataAsString);
            outboundPayload.setDataPayload((O) epc);
        }
        // Clear future stages.
        outboundPayload.getFuturePath().clear();

        return outboundPayload;
    }

}
