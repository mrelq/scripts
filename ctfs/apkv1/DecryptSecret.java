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

public class DecryptSecret {
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/CBC/PKCS5Padding";
    private static final String x0 = "AJGu2WsBngYm7H7S";
    private static final String x1 = "WdiNdBC2tMTa76BB";
    private static final String secret = "IzZGvYWoNBs5ejRhuJisRXjV244UIx7/Wv81OykpIgp=";

    public static void main(String[] args) {
        try {
            String decryptedSecret = d0(r0(secret, 5));
            System.out.println("Decrypted secret: " + decryptedSecret);
        } catch (Exception e) {
            System.err.println("Error decrypting secret: " + e.getMessage());
            e.printStackTrace();
        }
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

    public static String d0(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] decode = Base64.getDecoder().decode(str);
        SecretKeySpec secretKeySpec = new SecretKeySpec(x1.getBytes(), ALGORITHM);
        Cipher instance = Cipher.getInstance(MODE);
        instance.init(2, secretKeySpec, new IvParameterSpec(x0.getBytes()));
        return new String(instance.doFinal(decode));
    }
}
