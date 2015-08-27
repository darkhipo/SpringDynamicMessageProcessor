/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor.domain.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PathInitializationService implements PathInitializationServiceInterface{

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PathInitializationService.class.getName());
    private Map<Class<?>, List<String>> pathMap;

    @PostConstruct
    public void setup() {
        this.pathMap = new HashMap<Class<?>, List<String>>();
    }
    
    @Override
    public void register(Class<?> c, List<String> l) {
        this.pathMap.put(c, l);
    }

    @Override
    public void unregister(Class<?> c) {
        this.pathMap.remove(c);
    }

    @Override
    public <E> List<String> initializePath(E inbound) {
        return this.pathMap.get(inbound.getClass());
    }
    
    @Override
    public void clear() {
        this.pathMap.clear();
    }

}
