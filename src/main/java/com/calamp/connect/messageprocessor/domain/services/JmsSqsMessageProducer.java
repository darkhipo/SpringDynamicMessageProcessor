/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

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

@Service
public class JmsSqsMessageProducer {

    final static Logger logger = Logger.getLogger(JmsSqsMessageProducer.class);

    @Autowired
    SerializeDeserializeService sds;

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void sendMessage(final Serializable obj) {
        String txt = sds.objectToString(obj);
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