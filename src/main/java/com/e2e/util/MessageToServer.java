package main.java.com.e2e.util;  
public class MessageToServer {
    public byte[] encryptedMessage; // encrpted message that the server is supposed to decrypt for the metadata
    public byte[] encryptedAESkey; // encrypted AES key with the servers' public RSA key    
    public byte[] digitalSignature;

    public MessageToServer() {};
    public MessageToServer(byte[] encryptedMessage, byte[] encryptedAESkey, byte[] digitalSignature) {
        this.encryptedMessage = encryptedMessage;
        this.encryptedAESkey = encryptedAESkey;
        this.digitalSignature = digitalSignature;
    }
}
