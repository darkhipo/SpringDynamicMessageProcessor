/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.model;

import java.io.Serializable;

public class ClearFuturePathClass implements CapsuleInterface<String>, Serializable {

    private static final long serialVersionUID = 2291542835638709498L;
    private String payload;

    public ClearFuturePathClass(String payload) {
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
