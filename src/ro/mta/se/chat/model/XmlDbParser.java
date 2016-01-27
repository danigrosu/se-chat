package ro.mta.se.chat.model; /**
 *
 * Created by Dani on 11/23/2015.
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



public class XmlDbParser {

    public static  final String DATABASE_PATH = "docs/chat_database.xml";

    /**
     * Function that gets all friends from database
     * @return
     */
    public ArrayList<User> getAllFriends() {

        ArrayList<User> users = new ArrayList<>();
        try {

            File fXmlFile = new File(DATABASE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("friend");


            for (int temp = 0; temp < nList.getLength(); temp++) {


                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    User user = new User(eElement.getAttribute("id"), eElement.getAttribute("name"),
                            eElement.getAttribute("ip"), eElement.getAttribute("port"));

                    users.add(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Get user information
     * @param userName Username
     * @return User object
     */
    public User getUserData(String userName)
    {
        User user = null;

        try {

            File fXmlFile = new File(DATABASE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("friend");



            for (int temp = 0; temp < nList.getLength(); temp++) {


                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    if(eElement.getAttribute("name").equals(userName) )
                        user = new User(eElement.getAttribute("id"), eElement.getAttribute("name"),
                                eElement.getAttribute("ip"), eElement.getAttribute("port"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Adds a new user to database
     * @param user
     */
    public void addUser(User user)
    {
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(DATABASE_PATH);
            Element root = document.getDocumentElement();

            String countStr = root.getAttribute("count");
            int count = Integer.parseInt(countStr);

            Element friend = document.createElement("friend");
            friend.setAttribute("id", (++count) + "");
            friend.setAttribute("name", user.getName());
            friend.setAttribute("ip", user.getIp());
            friend.setAttribute("port", user.getPort());
            root.appendChild(friend);

            root.setAttribute("count",count + "");


            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(DATABASE_PATH);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Edits user's information
     * @param username
     * @param newUsername
     * @param ip
     * @param port
     */
    public void editUser(String username, String newUsername, String ip, String port){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(DATABASE_PATH);
            Element root = document.getDocumentElement();

            NodeList list = root.getElementsByTagName("friend");

            for (int i = 0; i < list.getLength(); i++){
                Node node = list.item(i);
                if (username.equals(node.getAttributes().getNamedItem("name").getTextContent())){
                    node.getAttributes().getNamedItem("name").setTextContent(newUsername);
                    node.getAttributes().getNamedItem("ip").setTextContent(ip);
                    node.getAttributes().getNamedItem("port").setTextContent(port);
                }

            }

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(DATABASE_PATH);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}