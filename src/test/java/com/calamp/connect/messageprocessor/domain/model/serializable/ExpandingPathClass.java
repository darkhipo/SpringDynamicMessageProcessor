package com.calamp.connect.messageprocessor.domain.model.serializable;

import java.io.Serializable;

public class ExpandingPathClass implements Serializable {

    private static final long serialVersionUID = 318764014940011810L;
    private String payload;

    public ExpandingPathClass(String payload) {
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
