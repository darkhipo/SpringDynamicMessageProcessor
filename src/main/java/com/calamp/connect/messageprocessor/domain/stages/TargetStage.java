package com.calamp.connect.messageprocessor.domain.stages;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

public class TargetStage extends Stage {

    public TargetStage(String ident) {
        super(ident);
    }

    @Override
    public <E> ProcessingWrapper<E> enact(ProcessingWrapper<E> inPayload) {
        return null;
    }

}
