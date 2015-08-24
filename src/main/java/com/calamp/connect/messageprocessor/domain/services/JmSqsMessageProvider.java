package com.calamp.connect.messageprocessor.domain.services;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("myMessageProviderService")
public class JmSqsMessageProvider {

    final static Logger logger = Logger.getLogger(JmSqsMessageProvider.class);

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String txt) {
        logger.debug(String.format("Sending message with txt: %s", txt));
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                final TextMessage msg = session.createTextMessage(txt);
                return msg;
            }
        });
        logger.debug("Message sent " + txt);
    }
}