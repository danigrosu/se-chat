package ro.mta.se.chat.controller.crypto;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.proxy.DatabaseProxy;
import ro.mta.se.chat.utils.DataConversion;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.login.Configuration;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;


/**
 * Created by Dani on 12/21/2015.
 */
public class AESManager {

    /**
     * AES encryption
     *
     * @param key AES key
     * @param initVector Initialization vector
     * @param value Input text for encrypting
     * @return base64 encrypted text
     */
    public static String encrypt(String key, String initVector, String value) {
        try {

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encode(encrypted);
        } catch (Exception ex) {
            Logger.log(Level.ERROR, "Exception occurred", ex);
        }

        return null;
    }

    /**
     * AES decryption
     *
     * @param key AES key
     * @param initVector Initialization vector
     * @param encrypted Encrypted base64 input
     * @return decrypted text
     */
    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            Logger.log(Level.ERROR, "Exception occurred", ex);
        }

        return null;
    }

}
