package com.calamp.connect.messageprocessor.testing.environment;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.model.serializable.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExceptionPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.serializable.StaticPathClass;
import com.calamp.connect.messageprocessor.domain.services.PathInitializationService;

public class TestEnv {

    private static final Logger log = Logger.getLogger(TestEnv.class.getName());
    private static BlockingQueue<String> testResponseQueue;
    
    static {
        testResponseQueue = new LinkedBlockingQueue<String>();
    }

    public static void bootPathService( PathInitializationService pis ){
        //See AppConfig.java for reference to these names.
        pis.register(StaticPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E")); 
        pis.register(ExpandingPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E", "DummyStage_F")); 
        pis.register(ClearFuturePathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_G", "DummyStage_B", "DummyStage_D", "DummyStage_E"));
        pis.register(ExceptionPathClass.class, Arrays.asList("DummyStage_B", "DummyStage_A", "DummyStage_H", "DummyStage_D"));
    }
    
    public static <E> void pushResponse(Future<ProcessingWrapper<E>> fresp) {
        log.info("Push Response!");
        try {
            ProcessingWrapper<E> resolved = fresp.get();
            String sresp = null;
            if (resolved.getWrappedException() != null){
                sresp = ExceptionUtils.getStackTrace( resolved.getWrappedException());
            }
            else{
                sresp = resolved.getDataPayload().toString();
            }
            log.info("Future Resolved " + sresp);
            TestEnv.testResponseQueue.add(sresp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static String pullResponse() {
        try {
            String took = TestEnv.testResponseQueue.take();
            log.info("Take Future " + took);
            return took;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
