/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.model;

import java.io.Serializable;

public class ExceptionPathClass implements CapsuleInterface<String>, Serializable {

    private static final long serialVersionUID = 3529697566646614421L;
    private String payload;

    public ExceptionPathClass(String payload) {
        this.setPayload(payload);
    }

    @Override
    public String getPayload() {
        return payload;
    }
    
    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return payload;
    }

}
