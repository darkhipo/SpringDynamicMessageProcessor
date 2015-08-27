package com.calamp.connect.messageprocessor.domain.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SQSerializeDeserializeService implements ObjectSerializerServiceInterface {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(SQSerializeDeserializeService.class.getName());
    private Base64 b64;

    @PostConstruct
    void setup() {
        this.b64 = new Base64();
    }

    @Override
    public String objectToString(Serializable object) {
        String encoded = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            encoded = new String(this.b64.encode(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded;
    }

    @SuppressWarnings("unchecked")
    public <t extends Serializable, T> T stringToObject(String string) {
        byte[] bytes = this.b64.decode(string.getBytes());
        T object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (T) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String serializeToSqs(Serializable obj) {
        return objectToString(obj);
    }

    public <T> T deserializeFromSqs(String fromSqs) {
        return stringToObject(fromSqs);
    }
}
