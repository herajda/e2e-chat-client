package com.e2e.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static final String SERVER = "localhost";
    private static final int PORT = 12345;
    private static final int MAX_MESSAGES = 15;

    private static boolean running = true;
    public static String clientName = "";
    
    private static final MessageHandler messageHandler = new MessageHandler(MAX_MESSAGES);
    private static final DisplayManager displayManager = new DisplayManager(messageHandler);
    private static final InputHandler inputHandler = new InputHandler();

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER, PORT)) {
            ConnectionManager connectionManager = new ConnectionManager(socket, messageHandler, displayManager);
            
            // Get client name
            connectionManager.handleClientNameInput();

            // Start reading messages from server in a separate thread
            Thread readThread = new Thread(connectionManager::readMessages);
            readThread.start();

            // Handle user input for sending messages
            inputHandler.handleUserInput(connectionManager);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isRunning() {
        return running;
    }
    public static void setRunning(boolean running) {
        Client.running = running;
    }
}
