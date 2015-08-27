/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.model.ClearFuturePathClass;
import com.calamp.connect.messageprocessor.domain.model.ExceptionPathClass;
import com.calamp.connect.messageprocessor.domain.model.ExpandingPathClass;
import com.calamp.connect.messageprocessor.domain.model.StaticPathClass;
import com.calamp.connect.messageprocessor.domain.services.JmsSqsMessageProducer;
import com.calamp.connect.messageprocessor.domain.services.PathInitializationService;
import com.calamp.connect.messageprocessor.domain.services.PathInitializationServiceInterface;
import com.calamp.connect.messageprocessor.domain.services.ReplyProcessServiceInterface;
import com.calamp.connect.messageprocessor.domain.services.SQSConnectionService;
import com.calamp.connect.messageprocessor.domain.services.TestingReplyProcessService;
import com.calamp.connect.messageprocessor.exceptions.ExpectedTestException;
import com.calamp.connect.messageprocessor.web.config.Application;

import org.springframework.test.annotation.DirtiesContext.ClassMode;

/**
 * README! If you run this sequence of tests the test program WILL terminate,
 * but it will take some time (about 5 seconds). I think the delay is just the
 * amount of time it takes to shutdown spring boot. In other words, you do not
 * need to terminate the test process; it will self terminate.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@IntegrationTest({ "server.port=0" })
@WebAppConfiguration
public class BaseTestSuite {

    private static Logger log = Logger.getLogger(SQSConnectionService.class.getName());

    @Autowired(required = true)
    JmsSqsMessageProducer provider;

    @Autowired(required = true)
    PathInitializationServiceInterface pathServe;
    
    @Autowired(required = true)
    ReplyProcessServiceInterface replyServe;

    @Value("http://localhost:${local.server.port}/")
    private URL base;

    @Before
    public void setUp() throws Exception {
        this.pathServe.register(StaticPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E")); 
        this.pathServe.register(ExpandingPathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_B", "DummyStage_D", "DummyStage_E", "DummyStage_F")); 
        this.pathServe.register(ClearFuturePathClass.class, Arrays.asList("DummyStage_C", "DummyStage_A", "DummyStage_G", "DummyStage_B", "DummyStage_D", "DummyStage_E"));
        this.pathServe.register(ExceptionPathClass.class, Arrays.asList("DummyStage_B", "DummyStage_A", "DummyStage_H", "DummyStage_D"));
    }

    @After
    public void tearDown() throws Exception {
        this.pathServe.clear();
    }

    @Test
    public void test1SpringBootRuns() throws Exception {
        RestTemplate template = new TestRestTemplate();
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), equalTo(Constants.bootOkString));
    }

    @Test
    public void test2PathStagesExecuteInOrder() throws Exception {
        String testMessage = "Hello!";
        StaticPathClass payload = new StaticPathClass(testMessage);
        String dummyStageOrder = " ";
        for (String s : this.pathServe.initializePath(payload)) {
            dummyStageOrder += s + " ";
        }
        dummyStageOrder = dummyStageOrder.substring(0, dummyStageOrder.length() - 1);

        provider.sendMessage(payload);
        String response = this.replyServe.pullResponse();
        log.info("2-GotResp : " + response);
        log.info("2-NeedResp: " + testMessage + dummyStageOrder);
        assertThat(response, equalTo(testMessage + dummyStageOrder));
    }

    @Test
    public void test3PathStagesExpand() throws Exception {
        String testMessage = "Hello!";
        ExpandingPathClass payload = new ExpandingPathClass(testMessage);
        String dummyStageOrder = " ";
        for (String s : this.pathServe.initializePath(payload)) {
            dummyStageOrder += s + " ";
        }
        dummyStageOrder += "DummyStage_A"; // Due to expansion!

        provider.sendMessage(payload);
        String response = this.replyServe.pullResponse();
        log.info("3-GotResp : " + response);
        log.info("3-NeedResp: " + testMessage + dummyStageOrder);
        assertThat(response, equalTo(testMessage + dummyStageOrder));
    }

    @Test
    public void test4ClearFuturePath() throws Exception {
        String testMessage = "Hello!";
        ClearFuturePathClass payload = new ClearFuturePathClass(testMessage);
        String dummyStageOrder = " DummyStage_C DummyStage_A DummyStage_G";
        provider.sendMessage(payload);
        String response = this.replyServe.pullResponse();
        log.info("4-GotResp : " + response);
        log.info("4-NeedResp: " + testMessage + dummyStageOrder);
        assertThat(response, equalTo(testMessage + dummyStageOrder));
    }

    @Test
    public void test5ExceptionEncountered() throws Exception {
        String testMessage = "Hello!";
        ExceptionPathClass payload = new ExceptionPathClass(testMessage);
        provider.sendMessage(payload);
        String response = this.replyServe.pullResponse();
        log.info("5-GotResp : " + response);
        log.info("5-NeedResp: Containts ExpectedTestException");
        assertTrue(response.contains(ExpectedTestException.class.getName()));
    }
}
