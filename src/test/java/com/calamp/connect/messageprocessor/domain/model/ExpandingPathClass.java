/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.model;

import java.io.Serializable;

public class ExpandingPathClass implements CapsuleInterface<String>, Serializable {

    private static final long serialVersionUID = 318764014940011810L;
    private String payload;

    public ExpandingPathClass(String payload) {
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
