package ro.mta.se.chat.communication;

import ro.mta.se.chat.model.CurrentConfiguration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * Created by Dani on 1/5/2016.
 */


public class PeerToPeerConnection {

    public static ConcurrentHashMap<String, Socket> currentPartners = new ConcurrentHashMap<>();
    protected int port = Integer.parseInt(CurrentConfiguration.getTheConfiguration().getPort());
    protected String username = CurrentConfiguration.getTheConfiguration().getUsername();
    protected String ip = CurrentConfiguration.getTheConfiguration().getIp();
    protected ServerSocket socket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public Socket connectToPeer(String ip, int port){
        try {
            Socket s = new Socket(ip, port);

            currentPartners.put(ip + ":" + port, s);

            OutputStream output = s.getOutputStream();
            output.write(("#PORT#" + this.port + "-" + this.username).getBytes());

            return s;

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void sendText(String text, Socket socket){
        try {
            OutputStream output = socket.getOutputStream();
            output.write(text.getBytes());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void acceptConnection(){
        try {
            openServerSocket();
            synchronized(this){
                this.runningThread = Thread.currentThread();
            }

            while (true) {
                Socket peerSocket = socket.accept();
                new Thread(new WorkerRunnable(peerSocket)).start();

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.socket = new ServerSocket(this.port);
            System.out.println("Listening on " + this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.port, e);
        }
    }

    public static String getStringFromInputStream(InputStream is) throws IOException {
        byte[] in = new byte[256];
        int size = is.read(in);
        String out = new String(in,0,size);
        //return out.replace("\n","").replace("\r", "");
        return out;
    }

}
