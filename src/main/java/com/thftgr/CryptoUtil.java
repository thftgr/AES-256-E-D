package com.thftgr;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtil {

    public String keygen(String SSS, int hashnum) throws Exception {
        String temp1 = SSS;
        for (int i = 0; i < hashnum; i++) {
            temp1 = new CryptoUtil().sha256(temp1);
        }
        System.out.println("hash_end:  " + temp1);
        return temp1;
    }


    public String sha256(String msg) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        return new CryptoUtil().byteToHexString(md.digest());
    }

    public String encryptAES256(String msg, String key) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        byte[] saltBytes = bytes;

        // Password-Based Key Derivation function 2
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        // 70000번 해시하여 256 bit 길이의 키를 만든다.
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);

        SecretKey secretKey = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // 알고리즘/모드/패딩
        // CBC : Cipher Block Chaining Mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        // Initial Vector(1단계 암호화 블록용)
        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));

        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

        return Base64.getEncoder().encodeToString(buffer);
    }

    public String decryptAES256(String msg, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));

        byte[] saltBytes = new byte[20];
        buffer.get(saltBytes, 0, saltBytes.length);
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        buffer.get(ivBytes, 0, ivBytes.length);
        byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
        buffer.get(encryoptedTextBytes);
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded(), "AES"), new IvParameterSpec(ivBytes));
        return new String(cipher.doFinal(encryoptedTextBytes));
    }

    public String byteToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public String encrypt(String input) throws Exception {

        String output = "";
        StringBuffer sb = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(input.getBytes());
        byte[] msgb = md.digest();
        for (int i = 0; i < msgb.length; i++) {
            byte temp = msgb[i];
            String str = Integer.toHexString(temp & 0xFF);
            while (str.length() < 2) {
                str = "0" + str;
            }
            str = str.substring(str.length() - 2);
            sb.append(str);
        }
        return sb.toString();
    }

}
