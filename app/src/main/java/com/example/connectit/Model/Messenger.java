package com.example.connectit.Model;

public class Messenger {
    String message,sender,reciever;
    long time;

    public Messenger(String message, String sender, String reciever, long time) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.time = time;
    }

    public Messenger() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
