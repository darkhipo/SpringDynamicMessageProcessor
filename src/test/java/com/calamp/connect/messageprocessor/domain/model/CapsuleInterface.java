/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.model;

public interface CapsuleInterface<I> {

    public I getPayload();

    public void setPayload(I payload);

}
