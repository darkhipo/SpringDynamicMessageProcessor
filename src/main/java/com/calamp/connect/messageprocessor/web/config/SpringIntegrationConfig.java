package com.calamp.connect.messageprocessor.web.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.stages.DummyStage;

@Configuration
@EnableIntegration
@ComponentScan( basePackages = { Constants.rootPackageName } )
@IntegrationComponentScan( basePackages = { Constants.rootPackageName } )
public class SpringIntegrationConfig {

    private static final Logger log = Logger.getLogger(SpringIntegrationConfig.class.getName());

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        log.info("Spring Poll: " + Constants.springPollDelayMillis);
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(Constants.springPollDelayMillis));
        return pollerMetadata;
    }

    @Bean
    public PublishSubscribeChannel calAmpSourceChannel() {
        log.info("Bean: calAmpSourceChannel");
        return new PublishSubscribeChannel();
    }

    @Bean
    public PublishSubscribeChannel calAmpStageChannel() {
        log.info("Bean: calAmpStageChannel");
        return new PublishSubscribeChannel();
    }

    @Bean
    public PublishSubscribeChannel calAmpTargetChannel() {
        log.info("Bean: calAmpTargetChannel");
        return new PublishSubscribeChannel();
    }

    @Bean
    public PublishSubscribeChannel calAmpReplyChannel() {
        log.info("Bean: calAmpReplyChannel");
        return new PublishSubscribeChannel();
    }

    @Bean
    public PublishSubscribeChannel calAmpErrorChannel() {
        log.info("Bean: calAmpErrorChannel");
        return new PublishSubscribeChannel();
    }
    
    ////////////Test Beans
    @Bean
    public DummyStage DummyStage_A() {
        log.info("Bean: DummyStage_A");
        return new DummyStage("A");
    }
    @Bean
    public DummyStage DummyStage_B() {
        log.info("Bean: DummyStage_B");
        return new DummyStage("B");
    }
    @Bean
    public DummyStage DummyStage_C() {
        log.info("Bean: DummyStage_C");
        return new DummyStage("C");
    }
    @Bean
    public DummyStage DummyStage_D() {
        log.info("Bean: DummyStage_D");
        return new DummyStage("D");
    }
    @Bean
    public DummyStage DummyStage_E() {
        log.info("Bean: DummyStage_E");
        return new DummyStage("E");
    }
}