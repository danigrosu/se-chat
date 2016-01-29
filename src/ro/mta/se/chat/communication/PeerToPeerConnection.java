package ro.mta.se.chat.communication;

import ro.mta.se.chat.controller.crypto.AESManager;
import ro.mta.se.chat.controller.crypto.DiffieHellmanFactory;
import ro.mta.se.chat.model.CurrentConfiguration;
import ro.mta.se.chat.model.MessageHistory;
import ro.mta.se.chat.observers.ChatRoomReloadable;
import ro.mta.se.chat.utils.Level;
import ro.mta.se.chat.utils.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dani on 1/5/2016.
 */

/**
 *  Class that keeps information about a peer
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


    /**
     *
     * @param username
     * @param ip
     * @param port
     * @return Socket for p2p communication
     * @throws IOException
     */
    public static synchronized Socket connectToPeer(String username, String ip, int port) throws IOException {

        if (CurrentConfiguration.getTheConfiguration().getIp().equals(ip) &&
                CurrentConfiguration.getTheConfiguration().getPort().equals(Integer.toString(port))) {
            throw new IOException("Cannot connect to yourself!");
        }

        PeerInfo pi = checkIfOpen(ip, Integer.toString(port));
        if (pi != null) {
            ChatRoomReloadable.reload(username);

            return pi.socket;
        }


        System.out.println("REACH CONNECT");
        Socket s;
        synchronized (PeerToPeerConnection.class) {
            s = new Socket(ip, port);
        }
        /// Diffie-Hellman

        DiffieHellmanFactory diffieHellmanFactory = new DiffieHellmanFactory();

        String a = diffieHellmanFactory.computePublic();
        String g = diffieHellmanFactory.getG();
        String p = diffieHellmanFactory.getP();


        OutputStream output = s.getOutputStream();

        if (s.isConnected())
            output.write(("#PORT#-" + CurrentConfiguration.getTheConfiguration().getPort() + "-" +
                    CurrentConfiguration.getTheConfiguration().getUsername() +
                    "-" + p + "-" + a).getBytes());

        new Thread(new ClientWorkerRunnable(s, username, diffieHellmanFactory)).start();

        return s;

    }

    /**
     *
     * @param text
     * @param ip
     * @param port
     */
    public static void sendText(String text, String ip, String port, String username) throws Exception {

        PeerInfo peerInfo = currentPartners.get(ip + ":" + port);

        if (peerInfo.socket.isClosed()) {
            throw new Exception("Partner " + ip + ":" + port + " left the chat");
        }

        OutputStream output = peerInfo.socket.getOutputStream();
        output.write(AESManager.encrypt(peerInfo.aesKey, "1111111100000000", text).getBytes());


        MessageHistory mh = new MessageHistory(username);
        mh.storeNewMessage(text, true);


    }

    public static void sendFileHead(String filename, String ip, String port, String username) throws Exception {
        PeerInfo peerInfo = currentPartners.get(ip + ":" + port);

        if (peerInfo.socket.isClosed()) {
            throw new Exception("Partner " + ip + ":" + port + " left the chat");
        }

        OutputStream output = peerInfo.socket.getOutputStream();
        output.write(("#FILE#:" + filename).getBytes());
    }

    public static void sendFileTail (String filename, String ip, String port, String username) throws Exception {
        PeerInfo peerInfo = currentPartners.get(ip + ":" + port);

        if (peerInfo.socket.isClosed()) {
            throw new Exception("Partner " + ip + ":" + port + " left the chat");
        }

        OutputStream output = peerInfo.socket.getOutputStream();
        output.write(("#FILEEOF#").getBytes());
    }

    public static void sendFile(String chunk, String ip, String port, String username) throws Exception {
        PeerInfo peerInfo = currentPartners.get(ip + ":" + port);
        if (peerInfo.socket.isClosed()) {
            throw new Exception("Partner " + ip + ":" + port + " left the chat");
        }

        OutputStream output = peerInfo.socket.getOutputStream();
        //output.write(AESManager.encrypt(peerInfo.aesKey, "1111111100000000", chunk).getBytes());
        chunk = "#FILECHUNK#" + chunk;
        output.write((chunk).getBytes());


    }

    /**
     *
     * @param myPort
     */
    public static void acceptConnection(int myPort) throws Exception{
            openServerSocket(myPort);

            while (true) {
                Socket peerSocket = socket.accept();

                new Thread(new ServerWorkerRunnable(peerSocket)).start();

            }
    }

    /**
     *
     * @return
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     *
     */
    public static synchronized void stop() {
        isStopped = true;
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     *
     * @param myPort
     */
    private static void openServerSocket(int myPort) throws Exception{

        socket = new ServerSocket(myPort);
        System.out.println("Listening on " + myPort);

    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String getStringFromInputStream(InputStream is) throws IOException {
        byte[] in = new byte[2048];
        int size = is.read(in);
        String out = new String(in, 0, size);
        //return out.replace("\n","").replace("\r", "");
        return out;
    }

    public static String getStringFromInputStream(InputStream is, int chunkSize) throws IOException {
        byte[] in = new byte[2048];
        int size = is.read(in,0,chunkSize);
        String out = new String(in, 0, size);
        //return out.replace("\n","").replace("\r", "");
        return out;
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        byte[] in = new byte[2048];
        int size = is.read(in);
        return in;
    }

    private static PeerInfo checkIfOpen(String ip, String port) {
        PeerInfo pi = currentPartners.get(ip + ":" + port);
        return pi;
    }

}
