package ro.mta.se.chat.controller.crypto;

import sun.security.provider.MD5;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.lang.Object;
import javax.crypto.Cipher;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


/**
 *
 * Created by Dani on 12/2/2015.
 */
public class RSAKeysManager {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = "src/private_key.bin";

    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = "src/public_key.bin";

    /**
     *
     * @param username Username
     * @param password Password
     */
    public static void createLoginToken(String username, String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hex = (new HexBinaryAdapter()).marshal(md.digest((username + password).getBytes()));

            generateKey(hex);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hex = (new HexBinaryAdapter()).marshal(md.digest((username + password).getBytes()));

            PrivateKey privateKey = getPrivateKey(hex);
            if(privateKey != null)
                return true;
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }

    private static byte[] getFileBytes(String infile){
        File f = new File(infile) ;
        int sizecontent = ((int) f.length());
        byte[] data = new byte[sizecontent];
        try
        {
            FileInputStream freader = new FileInputStream(f);
            freader.read(data, 0, sizecontent) ;
            freader.close();
            return data;
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.toString());
            return null;
        }
    }

    private static void displayData(byte[] data)
    {
        int bytecon = 0;    //to get unsigned byte representation
        for(int i=1; i<=data.length ; i++){
            bytecon = data[i-1] & 0xFF ;   // byte-wise AND converts signed byte to unsigned.
            if(bytecon<16)
                System.out.print("0" + Integer.toHexString(bytecon).toUpperCase() + " ");   // pad on left if single hex digit.
            else
                System.out.print(Integer.toHexString(bytecon).toUpperCase() + " ");   // pad on left if single hex digit.
            if(i%16==0)
                System.out.println();
        }
    }

    public static void savePublicKeyToDisk(PublicKey publicKey){
        try {
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();


            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(publicKey);
            publicKeyOS.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void savePrivateKeyToDisk(PrivateKey privateKey, String hash){

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

            FileOutputStream fos = new FileOutputStream(PRIVATE_KEY_FILE);
            fos.write(encryptedPkcs8);
            fos.close();

        }
        catch (Exception e){
            e.printStackTrace();
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
    public static void generateKey(String hash) {
        try {


            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            savePrivateKeyToDisk(key.getPrivate(), hash);
            savePublicKeyToDisk(key.getPublic());


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static PublicKey getPublicKey(){

        try {

            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            inputStream.close();

            //byte[] bytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
            return publicKey;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static PrivateKey getPrivateKey(String passwd){
        try {

            byte[] theData = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));

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
        }
        catch (Exception e){
            return null;
        }
    }
    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public static boolean areKeysPresent() {

        File privateKey = new File(PRIVATE_KEY_FILE);
        File publicKey = new File(PUBLIC_KEY_FILE);

        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The public key
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
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @param key
     *          :The private key
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
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }

}
