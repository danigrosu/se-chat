package ro.mta.se.chat.controller.crypto;

import ro.mta.se.chat.utils.Constants;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


/**
 * Created by Dani on 12/2/2015.
 */
public class RSAKeysManager {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * @param username Username
     * @param password Password
     */
    public static void createLoginToken(String username, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hex = (new HexBinaryAdapter()).marshal(md.digest((username + password).getBytes()));

            generateKey(hex, username);
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }
    }

    /**
     * @param username Username
     * @param password Password
     * @return true if login is successful
     */
    public static boolean login(String username, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hex = (new HexBinaryAdapter()).marshal(md.digest((username + password).getBytes()));

            PrivateKey privateKey = getPrivateKey(hex, username);
            if (privateKey != null)
                return true;
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());

        }
        return false;
    }


    /**
     * Function that saves the public key to file
     *
     * @param publicKey public key
     */
    public static void savePublicKeyToDisk(PublicKey publicKey, String username) {
        try {
            File publicKeyFile = new File(Constants.PUBLIC_KEY_FILE + username + ".bin");

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();


            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(publicKey);
            publicKeyOS.close();

        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }
    }

    /**
     * @param privateKey private key
     * @param hash       hash username + password
     * @param username   username
     */
    public static void savePrivateKeyToDisk(PrivateKey privateKey, String hash, String username) {

        try {
            // extract the encoded private key, this is an unencrypted PKCS#8 private key
            byte[] encodedPrivateKey = privateKey.getEncoded();

            String MYPBEALG = "PBEWithSHA1AndDESede";

            int count = 20;// hash iteration count
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[8];
            random.nextBytes(salt);

            // Create PBE parameter set
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(hash.toCharArray());
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

            Cipher pbeCipher = Cipher.getInstance(MYPBEALG);

            // Initialize PBE Cipher with key and parameters
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

            // Encrypt the encoded Private Key with the PBE key
            byte[] cipherText = pbeCipher.doFinal(encodedPrivateKey);

            // Now construct  PKCS #8 EncryptedPrivateKeyInfo object
            AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
            algparms.init(pbeParamSpec);
            EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, cipherText);

            // and here we have it! a DER encoded PKCS#8 encrypted key!
            byte[] encryptedPkcs8 = encinfo.getEncoded();

            FileOutputStream fos = new FileOutputStream(Constants.PRIVATE_KEY_FILE + username + ".bin");
            fos.write(encryptedPkcs8);
            fos.close();

        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }
    }


    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void generateKey(String hash, String username) {
        try {


            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            savePrivateKeyToDisk(key.getPrivate(), hash, username);
            savePublicKeyToDisk(key.getPublic(), username);


        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }

    }

    /**
     * Reads the public key from file
     *
     * @return public key
     */
    public static PublicKey getPublicKey(String username) {

        try {

            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Constants.PUBLIC_KEY_FILE +
                    username + ".bin"));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            inputStream.close();

            return publicKey;
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
            return null;
        }

    }

    /**
     * Reads the private key from file
     *
     * @param passwd password
     * @return private key
     */
    public static PrivateKey getPrivateKey(String passwd, String username) {
        try {

            byte[] theData = Files.readAllBytes(Paths.get(Constants.PRIVATE_KEY_FILE + username + ".bin"));

            EncryptedPrivateKeyInfo encryptPKInfo = new EncryptedPrivateKeyInfo(theData);

            Cipher cipher = Cipher.getInstance(encryptPKInfo.getAlgName());
            PBEKeySpec pbeKeySpec = new PBEKeySpec(passwd.toCharArray());
            SecretKeyFactory secFac = SecretKeyFactory.getInstance(encryptPKInfo.getAlgName());
            Key pbeKey = secFac.generateSecret(pbeKeySpec);
            AlgorithmParameters algParams = encryptPKInfo.getAlgParameters();
            cipher.init(Cipher.DECRYPT_MODE, pbeKey, algParams);
            KeySpec pkcs8KeySpec = encryptPKInfo.getKeySpec(cipher);
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            return kf.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
            return null;
        }
    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent(String username) {

        File privateKey = new File(Constants.PRIVATE_KEY_FILE + username + ".bin");
        File publicKey = new File(Constants.PUBLIC_KEY_FILE + username + ".bin");

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text : original plain text
     * @param key  :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            Logger.log(Level.ERROR, "Exception occurred", e.getMessage());
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text :encrypted text
     * @param key  :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            Logger.log(Level.ERROR, "Exception occurred", ex.getMessage());
        }

        return new String(dectyptedText);
    }

    public static String getHash(String username, String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return (new HexBinaryAdapter()).marshal(md.digest((username + password).getBytes()));
    }

}
