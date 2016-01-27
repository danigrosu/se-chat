package ro.mta.se.chat.communication;

/**
 * Created by Dani on 1/5/2016.
 */

import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.controller.crypto.DiffieHellmanFactory;
import ro.mta.se.chat.observers.MessageObserver;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**

 */
public class WorkerRunnable implements Runnable {

    protected Socket clientSocket = null;
    protected MessageObserver messageObserver = null;
    protected DiffieHellmanFactory df = null;
    protected String ip = null;
    protected int port = 0;
    protected String username;
    String aesKey = null;
    int semaphore;

    /**
     * Constructor
     * @param clientSocket
     * @param username
     * @param semaphore
     * @param df
     */
    public WorkerRunnable(Socket clientSocket, String username, int semaphore, DiffieHellmanFactory df) {
        messageObserver = MessageObserver.getMessageObserver();
        this.semaphore = semaphore;
        this.clientSocket = clientSocket;

        if (semaphore == 1) {
            port = clientSocket.getPort();
            String token = clientSocket.getRemoteSocketAddress().toString().replace("/", "");
            int index = token.indexOf(":");
            ip = token.substring(0, index);
            this.df = df;
            this.username = username;
            //aesKey = PeerToPeerConnection.currentPartners.get(ip + ":" + port).aesKey;
        }
    }

    /**
     * Listen for new messages
     */
    public void run() {
        try {

            while (!clientSocket.isClosed()) {
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();

                if (semaphore == 0) {
                    String result = PeerToPeerConnection.getStringFromInputStream(input);

                    ip = clientSocket.getRemoteSocketAddress().toString().replace("/", "");
                    int index = ip.indexOf(":");
                    ip = ip.substring(0, index);

                    String[] tokens = result.split("-");
                    port = Integer.parseInt(tokens[1]);
                    this.username = tokens[2];

                    String p = tokens[3];
                    String a = tokens[4]; // peer public

                    df = new DiffieHellmanFactory();
                    String myPublic = df.computePublic(p);

                    output.write(("#PUB#-" + myPublic).getBytes());


                    String theKey = df.computeTheKey(a);

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    String hex = (new HexBinaryAdapter()).marshal(md.digest((theKey).getBytes()));

                    aesKey = hex.substring(10, 26);

                    System.out.println("Session key: " + aesKey);

                    PeerToPeerConnection.currentPartners.put(ip + ":" + port, new PeerInfo(clientSocket, aesKey));

                    // TODO: add listener
                    messageObserver.addListener(username, ip, Integer.toString(port));

                    semaphore++;
                } else if (semaphore == 1) {

                    String received = PeerToPeerConnection.getStringFromInputStream(input);

                    if (received.indexOf("#PUB#") == 0) {
                        String[] tokens = received.split("-");

                        String theKey = this.df.computeTheKey(tokens[1]);

                        MessageDigest md = MessageDigest.getInstance("MD5");
                        String hex = (new HexBinaryAdapter()).marshal(md.digest((theKey).getBytes()));

                        aesKey = hex.substring(10, 26);

                        System.out.println("Session key: " + aesKey);

                        PeerToPeerConnection.currentPartners.put(ip + ":" + port, new PeerInfo(clientSocket, aesKey));
                        messageObserver.addListener(username, ip, Integer.toString(port));
                    } else {

                        // TODO: notify view
                        messageObserver.notifyView(ip, Integer.toString(port),
                                AESManager.decrypt(aesKey, "1111111100000000", received));
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Partner: " + clientSocket.getRemoteSocketAddress().toString() + " left the chat.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null)
                    clientSocket.close();
            } catch (IOException e) {

                e.getMessage();
            }
        }
    }
}
