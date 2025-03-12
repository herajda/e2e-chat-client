package com.e2e.util;

import java.util.*;

public class MessageHandler {
    private final int maxMessages;
    private final List<String> messages;

    public MessageHandler(int maxMessages) {
        this.maxMessages = maxMessages;
        this.messages = new ArrayList<>();
    }

    public synchronized void addMessage(String message) {
        messages.add(message);
        if (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    public synchronized List<String> getMessages() {
        return new ArrayList<>(messages);
    }
}
