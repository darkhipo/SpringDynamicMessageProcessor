package com.calamp.connect.messageprocessor.domain.stages;

import java.util.Collections;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

public class DummyStage extends Stage {

    public DummyStage(String ident) {
        super(ident);
    }
    
    @Override
    public <E> ProcessingWrapper<E> enact(ProcessingWrapper<E> inPayload) {
        /*
         * TODO: Actual Work Would Happen Here. The outbound is a copy of the
         * inbound with the payload data and future path fields modified.
         */
        // Copy construction, outboundPayload is deep copy.
        ProcessingWrapper<E> outboundPayload = new ProcessingWrapper<E>( inPayload );
        // The first element of the future becomes the present.
        outboundPayload.advance( this.getStageIdentifer() );
        //String payloads used for testing.
        String dataAsString = null;
        if( inPayload.getDataPayload() instanceof String ){
            dataAsString = inPayload.getDataPayload() + " " + this.getStageIdentifer();
            outboundPayload.setDataPayload( (E) dataAsString );
        }
        // Modify outbound payload data.
        // The future (at outbound payload) beyond the present is random.
//        for (String s : outboundPayload.getTransitPath()) {
//            if (!outboundPayload.getFuturePath().contains(s)) {
//                outboundPayload.getFuturePath().add(s);
//            }
//        }
        Collections.shuffle( outboundPayload.getFuturePath() );

        return outboundPayload;
    }
}
