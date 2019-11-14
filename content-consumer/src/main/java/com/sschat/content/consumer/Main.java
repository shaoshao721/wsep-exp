package com.sschat.content.consumer;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        EnvProperties.initialize();
        Consumer.consume();

    }
}
