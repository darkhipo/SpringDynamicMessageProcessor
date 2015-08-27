/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.services;

public interface SQSConnectionServiceInterface {

    public void sendMessage(String spayload);

    public Boolean hasNextMessage();

    public String recieveMessage();

}
