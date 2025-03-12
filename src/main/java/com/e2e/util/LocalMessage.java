package main.java.com.e2e.util;

import java.security.interfaces.RSAPublicKey;

public class LocalMessage {
    public RSAPublicKey senderRsaPublicKey;
    public String text;
    public LocalMessage(RSAPublicKey senderRSAPublicKey, String text) {
        this.senderRsaPublicKey = senderRSAPublicKey;
        this.text = text;
    }
}
