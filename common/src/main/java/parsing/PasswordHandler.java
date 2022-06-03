package parsing;

import console.ConsoleOutputer;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * класс, который кодирует/декодирует пароли
 */
public class PasswordHandler {
private final static ConsoleOutputer o = new ConsoleOutputer();
    public static String encode(String s){

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            o.printRed("encoding troubles " + e.getMessage());
        }
        assert digest != null;
        byte[] hash = digest.digest(
                s.getBytes(StandardCharsets.UTF_8));

        return new String(Hex.encode(hash));
    }

    public static String decode(String s){

        MessageDigest digest1 = null;
        try {
            digest1 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            o.printRed("decoding troubles " + e.getMessage());
        }
        assert digest1 != null;
        byte[] hash = digest1.digest(
                s.getBytes(StandardCharsets.UTF_8));

        return new String(Hex.decode(hash));
    }
}
