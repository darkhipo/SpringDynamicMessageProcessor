/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.services;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

@Primary
@Service
public class TestingReplyProcessService implements ReplyProcessServiceInterface {

    private static final Logger log = Logger.getLogger(TestingReplyProcessService.class.getName());
    private BlockingQueue<String> testResponseQueue;
    
    @Autowired(required = true)
    private PathInitializationService pis;
    
    @PostConstruct
    public void setup(){
        this.testResponseQueue = new LinkedBlockingQueue<String>();
    }
    
    public <E> void pushResponse(Future<ProcessingWrapper<E>> fresp) {
        log.info("Push Response!");
        try {
            ProcessingWrapper<E> resolved = fresp.get();
            String sresp = null;
            if (resolved.getWrappedException() != null) {
                sresp = ExceptionUtils.getStackTrace(resolved.getWrappedException());
            } else {
                sresp = resolved.getDataPayload().toString();
            }
            log.info("Future Resolved " + sresp);
            this.testResponseQueue.add(sresp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String pullResponse() {
        try {
            String took = this.testResponseQueue.take();
            log.info("Take Future " + took);
            return took;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <E> void processReply(Future<ProcessingWrapper<E>> reply) {
        this.pushResponse(reply);
    }
}
