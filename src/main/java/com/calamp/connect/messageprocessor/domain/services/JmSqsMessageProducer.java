package com.calamp.connect.messageprocessor.domain.services;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("myMessageProviderService")
public class JmSqsMessageProducer {

    final static Logger logger = Logger.getLogger(JmSqsMessageProducer.class);

    @Autowired
    SQSerializeDeserializeService sds;
    
    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void sendMessage(final Serializable obj) {
        String txt = sds.serializeToSqs(obj);
        logger.info(String.format("Sending message with txt: %s", txt));

        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                final TextMessage msg = session.createTextMessage(txt);
                return msg;
            }
        });

        logger.info("Message sent " + txt);
    }

}