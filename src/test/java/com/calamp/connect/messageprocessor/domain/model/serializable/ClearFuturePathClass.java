package com.calamp.connect.messageprocessor.domain.model.serializable;

import java.io.Serializable;

public class ClearFuturePathClass implements Serializable {

    private static final long serialVersionUID = 2291542835638709498L;
    private String payload;

    public ClearFuturePathClass(String payload) {
        this.setPayload(payload);
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return payload;
    }

}
