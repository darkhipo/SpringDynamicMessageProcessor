/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.stages;

import javax.annotation.PostConstruct;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.transformers.Transforms;

public abstract class Stage {

    private final String stageIdentifer;

    @PostConstruct
    public void setup() {
        Transforms.registerStage(this);
    }

    public Stage(String ident) {
        this.stageIdentifer = ident;
        Transforms.registerStage(this);
    }

    public String getStageIdentifer() {
        return stageIdentifer;
    }
    /**
     * The execution of each Stage is performed in this method. 
     * 'I' is the class of the object wrapped in the incoming ProcessingWrapper.
     * 'O' is the class of the outgoing object  in the outgoing ProcessingWrapper.
     * In other words the Stage execution may involve a transformation of one 
     * class of object into another.
     * @param inPayload: inbound parameter wrapper.
     * @return outbound parameter wrapper (after stage transform).
     * @throws Exception
     */
    public abstract <I, O> ProcessingWrapper<O> enact(ProcessingWrapper<I> inPayload) throws Exception;

}
