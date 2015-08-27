/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.services;

import java.util.List;

public interface PathInitializationServiceInterface {

    public void register(Class<?> c, List<String> l);

    public void unregister(Class<?> c);

    public <E> List<String> initializePath(E inbound);
    
    public void clear();

}
