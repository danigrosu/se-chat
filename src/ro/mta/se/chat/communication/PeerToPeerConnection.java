package ro.mta.se.chat.communication;

import jdk.internal.util.xml.impl.Input;
import ro.mta.se.chat.adapters.DatabaseAdapter;
import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.controller.crypto.DiffieHellmanFactory;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.observers.MessageObserver;
import ro.mta.se.chat.utils.DataConversion;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dani on 1/5/2016.
 */
class PeerInfo {
    public PeerInfo(Socket socket, String aesKey) {
        this.aesKey = aesKey;
        this.socket = socket;
    }

    public Socket socket;
    public String aesKey;
}

public class PeerToPeerConnection {



    public static ConcurrentHashMap<String, PeerInfo> currentPartners = new ConcurrentHashMap<>();
    protected static ServerSocket socket = null;
    protected static boolean isStopped = false;

    public static Socket connectToPeer(String username, String ip, int port) {
        try {

            System.out.println("REACH CONNECT");

            /// Diffie-Hellman

            DiffieHellmanFactory diffieHellmanFactory = new DiffieHellmanFactory();

            String a = diffieHellmanFactory.computePublic();
            String g = diffieHellmanFactory.getG();
            String p = diffieHellmanFactory.getP();

            Socket s = new Socket(ip, port);

            OutputStream output = s.getOutputStream();

            output.write(("#PORT#-" + CurrentConfiguration.getTheConfiguration().getPort() + "-" +
                    CurrentConfiguration.getTheConfiguration().getUsername() +
                    "-" + p + "-" + a).getBytes());

            new Thread(new WorkerRunnable(s, username, 1, diffieHellmanFactory)).start();

            return s;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendText(String text, String ip, String port) {
        try {

            PeerInfo peerInfo = currentPartners.get(ip+":"+port);

            OutputStream output = peerInfo.socket.getOutputStream();
            //output.write(AESManager.aesEncrypt(text.getBytes(), aesKey));
            output.write(AESManager.encrypt(peerInfo.aesKey, "1111111100000000", text).getBytes());
            //output.write(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void acceptConnection(int myPort) {
        try {
            openServerSocket(myPort);


            while (true) {
                Socket peerSocket = socket.accept();

                DiffieHellmanFactory df = new DiffieHellmanFactory();

                new Thread(new WorkerRunnable(peerSocket,null, 0, df)).start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public static synchronized void stop() {
        isStopped = true;
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private static void openServerSocket(int myPort) {
        try {
            socket = new ServerSocket(myPort);
            System.out.println("Listening on " + myPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + myPort, e);
        }
    }

    public static String getStringFromInputStream(InputStream is) throws IOException {
        byte[] in = new byte[2048];
        int size = is.read(in);
        String out = new String(in, 0, size);
        //return out.replace("\n","").replace("\r", "");
        return out;
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        byte[] in = new byte[2048];
        int size = is.read(in);
        return in;
    }

}
