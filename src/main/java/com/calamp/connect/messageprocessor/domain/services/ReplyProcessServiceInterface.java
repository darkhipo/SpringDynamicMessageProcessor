/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.services;

import java.util.concurrent.Future;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

public interface ReplyProcessServiceInterface {

    public <E> E pullResponse();

    public <E> void pushResponse(Future<ProcessingWrapper<E>> fresp);

}
