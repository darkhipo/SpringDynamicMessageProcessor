package com.calamp.connect.messageprocessor.domain.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;
import com.calamp.connect.messageprocessor.testing.environment.TestEnv;

@Service
public class PathInitializationService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PathInitializationService.class.getName());
    private Map<Class<?>, List<String>> pathMap;

    // TODO: This is a stub.
    @PostConstruct
    public void setup() {
        this.pathMap = new HashMap<Class<?>, List<String>>();
        // This Bootstraps a initial processing path for a class used in unit-testing.
        if (Constants.debug) {
            TestEnv.bootPathService(this);
        }
    }

    public void register(Class<?> c, List<String> l) {
        this.pathMap.put(c, l);
    }

    public void unregister(Class<?> c) {
        this.pathMap.remove(c);
    }

    public <E> List<String> initializePath(E inbound) {
        return this.pathMap.get(inbound.getClass());
    }

}
