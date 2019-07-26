package com.example.connectit.Model;

public class Messenger {
    String sender;
    String reciever;
    String message;
    Long time;

    public Messenger() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReceiver(String receiver) {
        this.reciever = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Messenger(String sender, String reciever, String message, Long time) {
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.time = time;
    }
}
