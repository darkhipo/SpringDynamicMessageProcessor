package com.calamp.connect.messageprocessor.domain.services;

public interface SimpleQueuServiceConnectionInterface {
    public void sendMessage(String spayload);
    public Boolean hasNextMessage();
    public String recieveMessage();
}
