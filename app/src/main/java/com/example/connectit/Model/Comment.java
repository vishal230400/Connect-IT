package com.example.connectit.Model;

public class Comment {
    public String comment,publisher;

    public Comment()
    {}

    public String getComment() {
        return comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public Comment(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }
}
