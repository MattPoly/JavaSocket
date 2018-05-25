/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class Serveur {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Serveur(int port) {
        this.setPort(port);
    }

    public void run() {
        try {
            DatagramSocket s = new DatagramSocket(this.getPort());
            System.out.println("Ouverture du port " + this.getPort());
            byte[] tabB = new byte[1024];

            while(true) {
                System.out.println("Serveur " + this.getPort() + " - En attente.");

                DatagramPacket dp = new DatagramPacket(tabB, tabB.length);
                s.receive(dp);

                System.out.println("Serveur " + this.getPort() + " - Communication en cours.");
                CommunicationThread communicationThread = new CommunicationThread(dp);
                communicationThread.start();

            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        Serveur serveur = new Serveur(2000);

        serveur.run();
    }
}
