package com.e2e.util;
import java.io.*;

import java.net.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ConnectionManager {
    private final Socket socket;
    private final MessageHandler messageHandler;
    private final DisplayManager displayManager;
    private PrintWriter writer;
    private BufferedReader reader;

    public ConnectionManager(Socket socket, MessageHandler messageHandler, DisplayManager displayManager) throws IOException {
        this.socket = socket;
        this.messageHandler = messageHandler;
        this.displayManager = displayManager;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void handleClientNameInput() throws IOException {
        String serverMessage;
        while ((serverMessage = reader.readLine()) != null) {
            System.out.println(serverMessage);
            if (serverMessage.startsWith("Enter your name:")) {
                String clientName = new Scanner(System.in).nextLine();
                writer.println(clientName);
                break;
            }
        }
    }

    public void readMessages() {
        try {
            String incomingMessage;
            while ((incomingMessage = reader.readLine()) != null) {
                messageHandler.addMessage(incomingMessage);
                displayManager.updateDisplay();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
        displayManager.updateDisplay();
    }
}
