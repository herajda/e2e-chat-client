package com.e2e.util;

import java.util.List;
import java.util.Arrays;
import com.e2e.client.*;

public class DisplayManager {
    private final MessageHandler messageHandler;

    public DisplayManager(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void updateDisplay() {
        clearScreen();
        printMessages();
        printInputField();
    }

    private void printMessages() {
        List<String> messages = messageHandler.getMessages();
        synchronized (messages) {
            int start = Math.max(0, messages.size() - 15);
            for (int i = start; i < messages.size(); i++) {
                String message = messages.get(i);
                if (message.startsWith(Client.clientName + ":")) {
                    String content = message.substring(Client.clientName.length() + 1).trim();
                    System.out.printf("%" + 50 + "s\n", content); // Right-aligned
                } else {
                    String[] parts = message.split(":", 2);
                    if (parts.length >= 1) {
                        String sender = parts[0].trim();
                        String content = parts.length > 1 ? parts[1].trim() : "";
                        String colored = getSenderColor(sender) + sender + "\u001B[0m: " + content;

                        int visibleLength = sender.length() + 2 + content.length();
                        int padding = 50 - visibleLength;
                        if (padding > 0) {
                            colored += " ".repeat(padding); // Add padding to align message
                        }
                        System.out.println(colored);
                    } else {
                        System.out.printf("%-50s\n", message);
                    }
                }
            }
        }
    }

    private void printInputField() {
        System.out.println("\n-------------------------");
        System.out.print("> ");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String getSenderColor(String sender) {
        // Define a list of color codes
        List<String> colorCodes = Arrays.asList(
                "\u001B[31m", // Red
                "\u001B[32m", // Green
                "\u001B[33m", // Yellow
                "\u001B[34m", // Blue
                "\u001B[35m", // Magenta
                "\u001B[36m" // Cyan
        );

        // Use hashCode() of the sender's name to determine a color
        int colorIndex = Math.abs(sender.hashCode()) % colorCodes.size();
        return colorCodes.get(colorIndex); // Return the corresponding color code
    }

}