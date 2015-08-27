/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.services;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;

@Service
public class ReplyProcessService implements ReplyProcessServiceInterface {

    private static Logger log = Logger.getLogger(ReplyProcessService.class.getName());

    @Override
    public <E> E pullResponse() {
        log.info("Process Reply Service pullResponse: null");
        return null;
    }

    @Override
    public <E> void pushResponse(Future<ProcessingWrapper<E>> fresp) {
        log.info("Process Reply Service pushResponse: " + fresp);
    }

}
