/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.model;

import java.io.Serializable;

public class StaticPathClass implements CapsuleInterface<String>, Serializable {

    private static final long serialVersionUID = 4531835932078304324L;
    private String payload;

    public StaticPathClass(String payload) {
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
