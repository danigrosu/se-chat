package ro.mta.se.chat.communication;

/**
 *
 * Created by Dani on 1/5/2016.
 */

import ro.mta.se.chat.view.ChatRoomPanel;
import ro.mta.se.chat.view.TabComponents;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;

    protected  String ip = null;
    protected  int port = 0;

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {

            while (!clientSocket.isClosed())
            {
                InputStream input = clientSocket.getInputStream();

                String result = PeerToPeerConnection.getStringFromInputStream(input);
                if (result.indexOf("#PORT#") == 0) {

                    System.out.println(result);

                    ip = clientSocket.getRemoteSocketAddress().toString().replace("/","");
                    int index = ip.indexOf(":");
                    ip = ip.substring(0,index);
                    String token = result.substring(6);
                    index = token.indexOf("-");
                    port = Integer.parseInt(token.substring(0, index));
                    String username = token.substring(index + 1);

                    PeerToPeerConnection.currentPartners.put(ip + ":" + port, clientSocket);

                    if ( TabComponents.getTabComponents("").isOpen(ip, port) == 0)
                        TabComponents.getTabComponents("").addPartner(username, ip, Integer.toString(port));
                }
                else
                {
                    LinkedList<ChatRoomPanel> list =  TabComponents.getTabComponents("").getTabList();

                    int i = 0;
                    for (; i < list.size(); i++) {
                        ChatRoomPanel chatRoomPanel = list.get(i);
                        if (chatRoomPanel.getPort() == port && chatRoomPanel.getIp().equals(ip)){
                            chatRoomPanel.appendTextAreaPrint(chatRoomPanel.getPartner() + ": " + result);
                            break;
                        }
                    }

                }

                //System.out.println(result);

            }
        }

        catch (IOException e)
        {
            System.out.println("Partner: " + clientSocket.getRemoteSocketAddress().toString() + " left the chat.");
        }
        finally
        {
            try
            {
                if (clientSocket != null)
                    clientSocket.close();
            }
            catch (IOException e) {

                e.getMessage();
            }
        }
    }
}
