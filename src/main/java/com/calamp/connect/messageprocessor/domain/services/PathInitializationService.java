package com.calamp.connect.messageprocessor.domain.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;

@Service
public class PathInitializationService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PathInitializationService.class.getName());
    private Map<Class<?>, List<String>> pathMap;

    // TODO: This is a stub.
    @PostConstruct
    public void setup() {
        this.pathMap = new HashMap<Class<?>, List<String>>();
        //This Bootstraps a initial processing path for a class used in unit-testing.
        //See AppConfig.java for reference to these names.
        this.pathMap.put(StaticPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E")); 
        this.pathMap.put(ExpandingPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E", "DummyStage_F")); 
        this.pathMap.put(ClearFuturePathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_G")); 
    }

    public <E> List<String> initializePath(E inbound) {
        return this.pathMap.get(inbound.getClass());
    }

}
