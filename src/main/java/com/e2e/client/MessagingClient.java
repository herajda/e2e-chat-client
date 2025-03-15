package com.e2e.client;
import java.io.*;
import java.net.*;
import java.security.*;

public class MessagingClient {
    private KeyPair keyPair;

    public MessagingClient() throws Exception {
        // Generate RSA key pair for the client
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        this.keyPair = keyGen.generateKeyPair();
    }

    public void connect(String host, int port) throws Exception {
        Socket socket = new Socket(host, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        // Step 1: Create and sign a nonce
        long clientNonce = System.currentTimeMillis();
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(keyPair.getPrivate());
        sig.update(Long.toString(clientNonce).getBytes());
        byte[] clientSignature = sig.sign();

        // Step 2: Send public key, signature, and nonce to server
        out.writeObject(keyPair.getPublic());
        out.writeObject(clientSignature);
        out.writeLong(clientNonce);
        out.flush();

        // Step 3: Receive server response
        String response = (String) in.readObject();
        System.out.println("Server response: " + response);

        if ("Handshake successful".equals(response)) {
            // Step 4: Receive and verify server's public key, nonce, and signature
            PublicKey serverPublicKey = (PublicKey) in.readObject();
            long serverNonce = in.readLong();
            byte[] serverSignature = (byte[]) in.readObject();
            System.out.println("Server public key: " + serverPublicKey);

            if (verifyServerSignature(serverPublicKey, serverSignature, serverNonce)) {
                System.out.println("Server verified successfully. Mutual authentication complete.");
            } else {
                System.out.println("Server verification failed.");
                socket.close();
            }
        } else {
            System.out.println("Handshake failed.");
            socket.close();
        }
    }


    public static void main(String[] args) throws Exception {
        MessagingClient client = new MessagingClient();
        client.connect("localhost", 12345);
    }
}
