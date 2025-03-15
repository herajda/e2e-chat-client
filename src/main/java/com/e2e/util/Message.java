package main.java.com.e2e.util;

import java.security.*;
public class Message {
    public byte[] encryptedAESkey; // encrypted AES key using a recepient public RSA
    public byte[] encryptedText; // encrypted text using AES key 
    public RSAPublicKey senderPublicKey; // RSA public key of the sender (for recepient to determine from whom is the message) 
    public byte[] digitalSignature; // signature of SHA256 hash of the text
    public Message() {};
    public Message(byte[] encryptedAESkey, byte[] encryptedText, RSAPublicKey senderPublicKey, byte[] digitalSignature) {
        this.encryptedAESkey = encryptedAESkey;
        this.encryptedText = encryptedText;
        this.senderPublicKey = senderPublicKey;
        this.digitalSignature = digitalSignature;
}
}
