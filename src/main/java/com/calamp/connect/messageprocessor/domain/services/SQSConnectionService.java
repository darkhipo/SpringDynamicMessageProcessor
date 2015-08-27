/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.services;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.calamp.connect.messageprocessor.Constants;

@Service
@ConfigurationProperties(prefix = Constants.sqsSetupYamlPrefix)
public class SQSConnectionService implements SQSConnectionServiceInterface {

    private static Logger log = Logger.getLogger(SQSConnectionService.class.getName());
    private AmazonSQS sqs;
    private Integer secondsPerPoll = 1;
    private String eventMessageOutUrl;
    private String sqsUrl;

    @PostConstruct
    public void setup() {
        log.info("SQSConnection Init " + sqsUrl + " " + eventMessageOutUrl);
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.", e);
        }
        this.sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        this.sqs.setRegion(usWest2);
    }

    public void sendMessage(String spayload) {
        log.info("sendMessage(String payload) ");
        log.info(spayload);
        SendMessageRequest mreq = new SendMessageRequest();
        mreq = mreq.withQueueUrl(this.sqsUrl);
        mreq = mreq.withMessageBody(spayload.toString());
        sqs.sendMessage(mreq);
    }

    public Boolean hasNextMessage() {
        ReceiveMessageRequest receiveMessageRequest;
        receiveMessageRequest = new ReceiveMessageRequest(this.getSqsUrl());
        receiveMessageRequest = receiveMessageRequest.withWaitTimeSeconds(this.getSecondsPerPoll());
        receiveMessageRequest = receiveMessageRequest.withMaxNumberOfMessages(1);
        ReceiveMessageResult mresp = sqs.receiveMessage(receiveMessageRequest);
        return (!mresp.getMessages().isEmpty());
    }

    /**
     * Receive message, then delete from Q. Return null if no message returned.
     */
    public String recieveMessage() {
        log.info("Receiving messages from SQS.");
        ReceiveMessageRequest receiveMessageRequest;
        receiveMessageRequest = new ReceiveMessageRequest(this.sqsUrl);
        receiveMessageRequest = receiveMessageRequest.withWaitTimeSeconds(this.secondsPerPoll);
        receiveMessageRequest = receiveMessageRequest.withMaxNumberOfMessages(1);
        ReceiveMessageResult mresp = sqs.receiveMessage(receiveMessageRequest);
        String payload = null;
        if (!mresp.getMessages().isEmpty()) {
            Message m = sqs.receiveMessage(receiveMessageRequest).getMessages().get(0);
            payload = m.getBody();
            log.info("Message Body: " + payload);
            log.info("Deleting a message: " + m.getReceiptHandle());
            DeleteMessageRequest dmr = new DeleteMessageRequest();
            dmr = dmr.withQueueUrl(this.sqsUrl);
            dmr = dmr.withReceiptHandle(m.getReceiptHandle());
            sqs.deleteMessage(dmr);
            log.info("Deleted: " + m.getReceiptHandle());
        }
        return payload;
    }

    public void setEventMessageOutUrl(String eventMessageOutUrl) {
        this.eventMessageOutUrl = eventMessageOutUrl;
        this.setSqsUrl(eventMessageOutUrl);
    }

    public Integer getSecondsPerPoll() {
        return secondsPerPoll;
    }

    public void setSecondsPerPoll(Integer secondsPerPoll) {
        this.secondsPerPoll = secondsPerPoll;
    }

    public String getSqsUrl() {
        return sqsUrl;
    }

    public void setSqsUrl(String sqsUrl) {
        this.sqsUrl = sqsUrl;
    }

}