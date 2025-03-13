package main.java.com.e2e.util;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    public static Message createMessage(String text, KeyPair senderRSAkey, RSAPublicKey recepientPublicKey) throws GeneralSecurityException {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);  // Set AES key size
            SecretKey secretKeyAES = keygen.generateKey();

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secretKeyAES);
            byte[] encryptedText = aesCipher.doFinal(text.getBytes());

            byte[] encryptedAESkey = getEncryptedAESKey(recepientPublicKey, secretKeyAES);
            byte[] signatureOfHashOfText = getSignatureOfHashOfText(senderRSAkey.getPrivate(), text);

            return new Message(encryptedAESkey, encryptedText, senderRSAkey.getPublic(), signatureOfHashOfText); 
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in creating message", e);
        }
    }

    public static LocalMessage getLocalMessage(Message message, RSAPrivateKey recepientPrivateKey) throws GeneralSecurityException {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.DECRYPT_MODE, recepientPrivateKey);
            byte[] decryptedAESKey = rsaCipher.doFinal(message.encryptedAESkey);

            SecretKey originalAESKey = new SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.length, "AES");
            String text = getTextFromMessage(message, originalAESKey);

            if (isSignatureOk(message.digitalSignature, message.senderPublicKey, text)) {
                return new LocalMessage(message.senderPublicKey, text);
            } else {
                throw new SignatureException("Invalid signature detected!");
            }
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in processing message", e);
        }
    }

    public static Boolean isSignatureOk(Signature digitalSignature, RSAPublicKey senderPublicKey, String text) throws GeneralSecurityException {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = sha256Digest.digest(text.getBytes());

            digitalSignature.initVerify(senderPublicKey);
            digitalSignature.update(messageHash);
            return digitalSignature.verify(messageHash);
        } catch (SignatureException e) {
            throw new SignatureException("Signature verification failed", e);
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in signature verification", e);
        }
    }

    private static String getTextFromMessage(Message message, SecretKey originalAESKey) throws GeneralSecurityException {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, originalAESKey);
            byte[] decryptedMessage = aesCipher.doFinal(message.encryptedText);
            return new String(decryptedMessage);
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in decrypting message", e);
        }
    }

    private static byte[] getEncryptedAESKey(RSAPublicKey recepientPublicKey, SecretKey aesKey) throws GeneralSecurityException {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, recepientPublicKey);
            return rsaCipher.doFinal(aesKey.getEncoded());
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in encrypting AES key", e);
        }
    }

    private static byte[] getSignatureOfHashOfText(RSAPrivateKey senderPrivateKey, String text) throws GeneralSecurityException {
        try {
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = sha256Digest.digest(text.getBytes());

            Signature rsaSignature = Signature.getInstance("SHA256withRSA"); 
            rsaSignature.initSign(senderPrivateKey);
            rsaSignature.update(messageHash);
            return rsaSignature.sign();
        } catch (Exception e) {
            throw new GeneralSecurityException("Error in signing message hash", e);
        }
    }
}
