package ro.mta.se.chat.controller.crypto;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Dani on 1/26/2016.
 */
public class DiffieHellmanFactory {

    String p;
    final String g = "3";
    String A;
    String a;
    String theKey;

    private String generateP() {
        BigInteger p = new BigInteger(1024, 99, new Random());
        this.p = p.toString();
        return this.p;
    }

    private String generatePrivate() {
        BigInteger A = new BigInteger(1024, new Random());
        this.A = A.toString();
        return this.A;
    }

    public String computePublic() {
        this.generateP(); // p
        this.generatePrivate(); // A

        BigInteger g = new BigInteger(this.g);
        BigInteger a = g.modPow(new BigInteger(this.A), new BigInteger(this.p));
        this.a = a.toString();
        return this.a;
    }

    public String computePublic(String p) {
        this.p = p;
        this.generatePrivate(); // A
        BigInteger pNumber = new BigInteger(this.p);

        BigInteger g = new BigInteger(this.g);
        BigInteger a = g.modPow(new BigInteger(this.A), pNumber);
        this.a = a.toString();
        return this.a;
    }

    public String computeTheKey(String peerPublic) throws NullPointerException {

        BigInteger peer = new BigInteger(peerPublic);
        BigInteger myPrivate = new BigInteger(this.A);
        BigInteger theKey = peer.modPow(myPrivate, new BigInteger(this.p));
        this.theKey = theKey.toString();
        return this.theKey;
    }

    public String getP() throws NullPointerException {
        return this.p;
    }

    public String getG() throws NullPointerException {
        return this.g;
    }

    public String getA() throws NullPointerException {
        return this.A;
    }

    public String getTheKey() throws NullPointerException {
        return this.theKey;
    }


}
