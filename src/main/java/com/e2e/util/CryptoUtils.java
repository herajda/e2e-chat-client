package main.java.com.e2e.util;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;
import main.java.com.e2e.util.Message;

public class CryptoUtils {

    public static Message createMessage(String text, KeyPair senderRSAkey, RSAPublicKey recepientPublicKey) {
        KeyGenerator AESkeygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey secretKeyAES = keyGen.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKeyAES);
        byte[] encryptedText = aesCipher.doFinal(text.getBytes());
        byte[] encryptedAESkey = getEncryptedAESKey(recepientPublicKey, secretKeyAES);
        byte[] signatureOfHashOfText = getEncryptedAESKey(senderRSAkey.getPrivate(), secretKeyAES);
        return Message(encryptedAESkey, encryptedText, senderRSAkey.getPublic(), signatureOfHashOfText); 
    }

    // returns the text of a message if the cryptography is OK
    public static LocalMessage getLocalMessage(Message message, RSAPrivateKey recepientPrivateKey) {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, recepientPrivateKey);
        byte[] decryptedAESKey = rsaCipher.doFinal(message.encryptedAESkey);
        SecretKey originalAESKey = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");
        String text = getTextFromMessage(message, originalAESKey);
        if (isSignatureOk(message.digitalSignature, message.senderPublicKey, text)) {
            return LocalMessage(message.senderPublicKey, text);
        }
        else {
            // the signature is bad -- tell the user and don't show the message
        }

    }
    public static Boolean isSignatureOk(Signature digitalSignature, RSAPublicKey senderPublicKey, Stirng text) {
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = sha256Digest.digest(text);

        digitalSignature.initVerify(senderPublicKey);
        rsaSignature.update(messageHash);
        return rsaSignature.verify(digitalSignature);
    }
    private static String getTextFromMessage(Message message, SecretKey originalAESKey) {
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalAESKey);
        byte[] decryptedMessage = aesCipher.doFinal(message.encryptedText);
        String decryptedMessageString = new String(decryptedMessage);
        return decryptedMessageString;
    }

    private static byte[] getEncryptedAESKey(RSAPublicKey recepientPublicKey, SecretKey aesKey) {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, recepientPublicKey);
        byte[] encryptedAESkey = rsaCipher.doFinal(aesKey.getEncoded());
        return encryptedAESkey;
    }
    private static byte[] getSignatureOfHashOfText(RSAPrivateKey senderPrivateKey, String text) {
        MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = sha256Digest.digest(text.getBytes());
        Signature rsaSignature = Signature.getInstance("SHA256withRSA"); 
        rsaSignature.initSign(senderPrivateKey);
        rsaSignature.update(messageHash);
        byte[] digitalSignature = rsaSignature.sign();
        return digitalSignature;
    }
}
