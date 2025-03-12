package com.e2e.util;
import java.util.Scanner;

import com.e2e.client.Client;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public void handleUserInput(ConnectionManager connectionManager) {
        while (Client.isRunning()) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                Client.setRunning(false); 
                connectionManager.sendMessage("exit");
                break;
            }
            connectionManager.sendMessage(message);
        }
    }
}
