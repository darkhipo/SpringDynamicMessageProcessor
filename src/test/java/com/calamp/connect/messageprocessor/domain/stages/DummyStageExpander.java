package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;
import com.calamp.connect.messageprocessor.domain.stages.Stage;

public class DummyStageExpander extends Stage{

    private DummyStage expandedHop;
    public DummyStageExpander(String ident, DummyStage expandTo) {
        super(ident);
        this.expandedHop = expandTo;
    }

    @Override
    public <I,O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) {
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
        //Add nexthop as expanding stage.
        outboundPayload.getFuturePath().add(0,this.expandedHop.getStageIdentifer());

        return outboundPayload;
    }

}
