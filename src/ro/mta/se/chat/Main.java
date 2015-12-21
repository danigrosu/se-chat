package ro.mta.se.chat;

import ro.mta.se.chat.controller.*;
import ro.mta.se.chat.view.*;


import javax.swing.*;

/**
 *
 * Created by Dani on 11/22/2015.
 */
public class Main {
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Turn off metal's use of bold fonts
                //UIManager.put("swing.boldMetal", Boolean.FALSE);
                //new view.TabComponents("TabComponentsDemo").runTest();


                Login login = new Login();


                /*
                JFrame frame = new MainFrame("LiveChat");
                frame.setSize(400,500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(false);
                */

            }
        });
    }
}

/*

Cateva modificari/rectificari in cazul proiectului la Software Engineering :
In ultimul laborator al grupei E214-C,precum si anterior prin email-uri l-am intrebat pe domnul profesor Cervicescu
cateva lucruri in legatura cu proiectl la SE si acesta mi-a oferit cateva hint-uri in privinta implementarii precum
si completari asupra enuntului proiectului.
Logarea Utilizatorului :
Va exista atat o parte de Inregistrare cat si logarea in aplicatia .
Inregistrarea , inseamna generarea unei chei private asimetrice si criptarea acesteia cu o cheie secreta ,aleasa la
inregistrare de catre user.Continutul criptat va fi salvat intr-un fisier pe calculatorul si ma fi memorata calea in
aplicatie catre fisirerul unde se afla cheia criptata.
Logarea inseamna efectiv intorducerea parolei (eventual a username-ului ,aici depinde de metoda de implementare ) si
decriptarea continutului fisierului criptat anterior.
Hint : Pentru generare chei exista comanda keytool -genkeypair, (pe care o puteti executa prin API-ul java ca un
process apenland cmd.exe) iar pentru decriptarea exista clasea KeyStore cu metoda getEntry care face decriptarea si
returneaza null in cazul in care decriptarea nu a avut loc cu succes.
De asemena varianta cu creearea unui certificat este "incurajata" ,in cazul in care veti dori sa creati si handshake-ul
aferent inainte de incepe schimbul de mesaje ( poate aduce puncte suplimentare ;
Conexiunea : Existenta unui Manager de Conexiuni ( un SockServer ) a carui principala functionalitate este de a trata
clienti.Astfel daca un user incearca sa ne trimita un mesaj ( invers e acelasi proces ) SockServerul va fi responsabil
pentru deschiderea unui canal de comunicare securizat.De asemena el va mentine si o lista cu toti socketii clienti.In
cazul in care noi incepem conexiunea procesul este similar.
Hint : Canalul asincron presupune stabilirea unei chei de conexiune ( handshake daca implementarea este cu CA ) si apoi
rularea asincron a unui thread in care vom citi/scrie intr-un Input/Output stream.
Strucutra pachetelor : In mare parte cam asa ar trebui sa arate pachetele tuturor :
->adapters
-> controllers
------>interfaces
------->abstracts
->crypto/security
->exceptions
->factory
---->abstracts
->models
----->abstracts
------>interfaces
->network/communications
->proxy
->utils
->views
---->abstracts
------>events
-------->interfaces
P.S. : Acesta este un sistem de pachete orientativ,insa este unul aprobat de domnul profesor.Modificari mai pot aparea .

 */