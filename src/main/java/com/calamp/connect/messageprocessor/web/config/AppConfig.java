package com.calamp.connect.messageprocessor.web.config;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.stages.DummyStage;
import com.calamp.connect.messageprocessor.domain.stages.DummyStageExpander;

@Configuration
@EnableIntegration
@ComponentScan(basePackages = { Constants.rootPackageName })
@IntegrationComponentScan(basePackages = { Constants.rootPackageName })
@ConfigurationProperties(prefix = Constants.sqsSetupYamlPrefix)
public class AppConfig {

    private static final Logger log = Logger.getLogger(AppConfig.class.getName());
    
    //These are set by reading in yaml via the @ConfigurationProperties annotation.
    private String eventMessageOutUrl;
    private String awsRegion;
    private Integer prefetchCount;

    ////// Spring-Integration Config
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

    ////// JMS-SQS Config
    @Bean
    public DefaultAWSCredentialsProviderChain credentialsProviderBean() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    public SQSConnectionFactory.Builder connectionFactoryBuilder() {
        SQSConnectionFactory.Builder b = new SQSConnectionFactory.Builder();
        b.setAwsCredentialsProvider(credentialsProviderBean());
        b.setRegionName(this.getAwsRegion());
        b.setNumberOfMessagesToPrefetch(this.getPrefetchCount());
        return b;
    }

    @Bean
    public SQSConnectionFactory connectionFactory() {
        return connectionFactoryBuilder().build();
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        String path = this.getEventMessageOutUrl();
        String qName = path.substring(path.lastIndexOf('/') + 1);

        JmsTemplate j = new JmsTemplate();
        j.setConnectionFactory(connectionFactory());
        j.setSessionTransacted(false); // SQS Does not support transacted.
        j.setDefaultDestinationName(qName);
        j.setReceiveTimeout(JmsTemplate.RECEIVE_TIMEOUT_NO_WAIT);
        return j;
    }

    ////// Yaml Conf
    public String getEventMessageOutUrl() {
        return eventMessageOutUrl;
    }

    public void setEventMessageOutUrl(String eventMessageOutUrl) {
        this.eventMessageOutUrl = eventMessageOutUrl;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public Integer getPrefetchCount() {
        return prefetchCount;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public void setPrefetchCount(Integer prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    ////// Test Beans
    @Bean
    public DummyStage DummyStage_A() {
        log.info("Bean: DummyStage_A");
        return new DummyStage("DummyStage_A");
    }

    @Bean
    public DummyStage DummyStage_B() {
        log.info("Bean: DummyStage_B");
        return new DummyStage("DummyStage_B");
    }

    @Bean
    public DummyStage DummyStage_C() {
        log.info("Bean: DummyStage_C");
        return new DummyStage("DummyStage_C");
    }

    @Bean
    public DummyStage DummyStage_D() {
        log.info("Bean: DummyStage_D");
        return new DummyStage("DummyStage_D");
    }

    @Bean
    public DummyStage DummyStage_E() {
        log.info("Bean: DummyStage_E");
        return new DummyStage("DummyStage_E");
    }
    
    @Bean
    public DummyStageExpander DummyStage_F() {
        log.info("Bean: DummyStage_F");
        return new DummyStageExpander("DummyStage_F", DummyStage_A());
    }

}