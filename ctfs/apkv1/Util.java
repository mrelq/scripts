package com.v2.onlinebanking;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Util {
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/CBC/PKCS5Padding";
    private static final String x0 = "AJGu2WsBngYm7H7S";
    private static final String x1 = "WdiNdBC2tMTa76BB";

    public static String encrypt(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(x1.getBytes(), ALGORITHM);
        Cipher instance = Cipher.getInstance(MODE);
        instance.init(1, secretKeySpec, new IvParameterSpec(x0.getBytes()));
        return Base64.getEncoder().encodeToString(instance.doFinal(str.getBytes()));
    }

    public static String d0(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] decode = Base64.getDecoder().decode(str);
        SecretKeySpec secretKeySpec = new SecretKeySpec(x1.getBytes(), ALGORITHM);
        Cipher instance = Cipher.getInstance(MODE);
        instance.init(2, secretKeySpec, new IvParameterSpec(x0.getBytes()));
        return new String(instance.doFinal(decode));
    }

    public static String r0(String str, int i) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c)) {
                char c2 = Character.isLowerCase(c) ? 'a' : 'A';
                sb.append((char) (((((c - c2) - i) + 52) % 26) + c2));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
