package com.calamp.connect.messageprocessor.domain.model.serializable;

import java.io.Serializable;

public class ExceptionPathClass implements Serializable {

    private static final long serialVersionUID = 3529697566646614421L;
    private String payload;

    public ExceptionPathClass(String payload) {
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
