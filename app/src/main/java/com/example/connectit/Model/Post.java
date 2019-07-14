package com.example.connectit.Model;

public class Post {
    String postid;
    String postimage;
    String description;
    String publisher;

    public Post()
    {}

    public String getPostid() {
        return postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public Post(String postid, String postimage, String description, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
    }
}
