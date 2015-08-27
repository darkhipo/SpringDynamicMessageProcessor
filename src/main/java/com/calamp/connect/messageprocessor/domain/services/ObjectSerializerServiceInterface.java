/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.services;

import java.io.Serializable;

public interface ObjectSerializerServiceInterface {

    public String objectToString(Serializable object);

    public <t extends Serializable, T> T stringToObject(String string);
}
