/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetsocket;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    public void run() {

        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Démarrage du client sur le port " + s.getLocalPort());

        InetAddress serveurAddress = null;
        try {
            serveurAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int serveurPort = 2000;

        while(true) {
            System.out.println("Message: (!quit pour quitter)");

            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();

            if(message.contains("!quit")) {
                System.out.println("Commande \"!quit\" reçue.");

                message = "!quit";
                DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), serveurAddress, serveurPort);

                System.out.println("Envoi vers le serveur (" + dp.getAddress().getHostName() + ":" + dp.getPort()+") depuis le port " + s.getLocalPort());
                try {
                    s.send(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                s.close();
                break;
            }

            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), serveurAddress, serveurPort);

            try {
                System.out.println("Envoi vers le serveur (" + dp.getAddress().getHostName() + ":" + dp.getPort()+") avec le port " + s.getLocalPort());
                s.send(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                System.out.println("En attente d'une réponse du serveur.");
                s.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            serveurAddress = dp.getAddress();
            serveurPort = dp.getPort();
            byte[] serveurMessage = dp.getData();

            System.out.println("Réponse reçue.");
            System.out.println("Serveur (" + serveurAddress + ":" + serveurPort + ") - Message : \"" + new String(serveurMessage) + "\"");
        }

        System.out.println("Au revoir.");

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

}
