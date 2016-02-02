package ro.mta.se.chat.communication;

/**
 * Created by Dani on 1/5/2016.
 */

import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.controller.crypto.DiffieHellmanFactory;
import ro.mta.se.chat.observers.MessageObserver;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**

 */
public class ClientWorkerRunnable implements Runnable {

    protected Socket peerSocket = null;
    protected MessageObserver messageObserver = null;
    protected DiffieHellmanFactory df = null;
    protected String ip = null;
    protected int port = 0;
    protected String username; // not me, the partner
    String aesKey = null;

    String filename = "";
    String fileString = "";

    /**
     * Constructor
     *
     * @param peerSocket
     * @param username
     * @param df
     */
    public ClientWorkerRunnable(Socket peerSocket, String username, DiffieHellmanFactory df) {

        this.peerSocket = peerSocket;

        port = peerSocket.getPort();
        String token = peerSocket.getRemoteSocketAddress().toString().replace("/", "");
        int index = token.indexOf(":");
        ip = token.substring(0, index);
        this.df = df;
        this.username = username;

        messageObserver = MessageObserver.getMessageObserver();
        messageObserver.addListener(username, ip, Integer.toString(port));
    }

    /**
     * Listen for new messages
     */
    public void run() {
        try {

            while (!peerSocket.isClosed()) {
                InputStream input = peerSocket.getInputStream();
                OutputStream output = peerSocket.getOutputStream();

                String received = PeerToPeerConnection.getStringFromInputStream(input, 523);

                if (received.indexOf("#PUB#") == 0) {
                    String[] tokens = received.split("-");

                    String theKey = this.df.computeTheKey(tokens[1]);

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    String hex = (new HexBinaryAdapter()).marshal(md.digest((theKey).getBytes()));

                    aesKey = hex.substring(10, 26);

                    System.out.println("Session key: " + aesKey);

                    PeerToPeerConnection.currentPartners.put(ip + ":" + port, new PeerInfo(peerSocket, aesKey));

                } else if (received.indexOf("#FILE#") == 0) {
                    filename = received.split(":")[1];
                    fileString = "";

                    messageObserver = MessageObserver.getMessageObserver();
                    messageObserver.addListener(username, ip, Integer.toString(port));
                    messageObserver.notifyView(username, ip, Integer.toString(port), "Receiving file:  " + filename);

                } else if (received.contains("#FILECHUNK#")) {

                    fileString += received.replace("#FILECHUNK#", "");

                    if (received.contains("#FILEEOF#")) {

                        fileString = fileString.replace("#FILEEOF#", "");

                        try {
                            ro.mta.se.chat.model.FileWriter.write(filename, fileString);

                            messageObserver = MessageObserver.getMessageObserver();
                            messageObserver.addListener(username, ip, Integer.toString(port));
                            messageObserver.notifyView(username, ip, Integer.toString(port), "File " + filename + " received");

                        } catch (Exception e) {
                            Logger.log(Level.ERROR, "Saving file to disk has failed");
                        }
                    }

                } else if (received.contains("#FILEEOF#")) {

                    fileString = received.replace("#FILEEOF#", "");

                    try {

                        ro.mta.se.chat.model.FileWriter.write(filename, fileString);

                        messageObserver = MessageObserver.getMessageObserver();
                        messageObserver.addListener(username, ip, Integer.toString(port));
                        messageObserver.notifyView(username, ip, Integer.toString(port), "File " + filename + " received");

                    } catch (Exception e) {
                        Logger.log(Level.ERROR, "Saving file to disk has failed");
                    }

                } else {

                    // TODO: notify view
                    messageObserver = MessageObserver.getMessageObserver();
                    messageObserver.addListener(username, ip, Integer.toString(port));

                    messageObserver.notifyView(username, ip, Integer.toString(port),
                            AESManager.decrypt(aesKey, "1111111100000000", received));
                }
            }

            //}
        } catch (IOException e) {
            Logger.log(Level.ERROR, "Exception occurred", e);
            if (messageObserver != null)
                messageObserver.notifyViewOnLeave(ip, Integer.toString(port));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                if (peerSocket != null)
                    peerSocket.close();
            } catch (IOException e) {

                Logger.log(Level.ERROR, "Exception occurred", e);
            }
        }
    }
}
