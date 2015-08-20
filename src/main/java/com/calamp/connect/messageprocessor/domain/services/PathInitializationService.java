package com.calamp.connect.messageprocessor.domain.services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.domain.stages.DummyStage;
import com.calamp.connect.messageprocessor.domain.transformers.Transforms;

@Service
public class PathInitializationService {

    //TODO: This is a stub.
    public <E> List<String> initializePath( E inbound ){
        List<String> initialPathPlan = Arrays.asList("C", "A", "B", "D", "E");
        return initialPathPlan;
    }
}
