package com.calamp.connect.messageprocessor.domain.model.serializable;

import java.io.Serializable;

public class StaticPathClass implements Serializable{

    private static final long serialVersionUID = 4531835932078304324L;
    private String payload;

    public StaticPathClass(String payload){
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
