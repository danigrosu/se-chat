package ro.mta.se.chat.controller.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * Created by Dani on 12/21/2015.
 */
public class AESManager {


    public static byte[] generateKey(int bits){
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // for example
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
