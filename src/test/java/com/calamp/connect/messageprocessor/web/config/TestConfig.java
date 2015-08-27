package com.calamp.connect.messageprocessor.web.config;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

import com.calamp.connect.messageprocessor.Constants;
import com.calamp.connect.messageprocessor.domain.stages.DummyStage;
import com.calamp.connect.messageprocessor.domain.stages.DummyStageClearer;
import com.calamp.connect.messageprocessor.domain.stages.DummyStageExceptor;
import com.calamp.connect.messageprocessor.domain.stages.DummyStageExpander;

@Configuration
@EnableIntegration
@ComponentScan(basePackages = { Constants.rootPackageName })
@IntegrationComponentScan(basePackages = { Constants.rootPackageName })
@ConfigurationProperties(prefix = Constants.sqsSetupYamlPrefix)
public class TestConfig {

    private static final Logger log = Logger.getLogger(TestConfig.class.getName());

    // //// Test Beans
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

    @Bean
    public DummyStageClearer DummyStage_G() {
        log.info("Bean: DummyStage_G");
        return new DummyStageClearer("DummyStage_G");
    }

    @Bean
    public DummyStageExceptor DummyStage_H() {
        log.info("Bean: DummyStage_H");
        return new DummyStageExceptor("DummyStage_H");
    }

}
