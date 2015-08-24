package com.calamp.connect.messageprocessor.domain.stages;

import javax.annotation.PostConstruct;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.transformers.Transforms;

public abstract class Stage {

    private final String stageIdentifer;

    @PostConstruct
    public void setup(){
        Transforms.registerStage(this);
    }

    public Stage( String ident ) {
        this.stageIdentifer = ident;
        Transforms.registerStage(this);
    }

    public String getStageIdentifer() {
        return stageIdentifer;
    }

    public abstract <E> ProcessingWrapper<E> enact(ProcessingWrapper<E> inPayload) throws Exception;
}