package com.calamp.connect.messageprocessor.domain.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service("myMessageConsumerService")
public class JmSqsMessageConsumer {

    final static Logger logger = Logger.getLogger(JmSqsMessageConsumer.class);

    @Autowired
    SQSerializeDeserializeService sds;

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public String readMessage() throws JMSException {
        logger.info("Reading message");
        Message msg = jmsTemplate.receive();

        String ret = null;
        if (msg instanceof TextMessage) {
            TextMessage txtmsg = (TextMessage) msg;
            ret = sds.deserializeFromSqs(txtmsg.getText());
            logger.info(String.format("Received text: %s", ret));
        }

        if (msg != null) {
            logger.info("Message Consumer Class: " + msg.getClass());
        }

        logger.info("Message Consumer Done");
        return ret;
    }

}