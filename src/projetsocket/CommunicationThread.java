/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

class CommunicationThread extends Thread {
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    private DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    private void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }



    public CommunicationThread(DatagramPacket datagramPacket) {
        this.setDatagramPacket(datagramPacket);
        try {
            this.setDatagramSocket(new DatagramSocket());
            System.out.println("Nouveau thread (port: " + this.getDatagramSocket().getLocalPort() + " ) créé");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread (port: " + this.getDatagramSocket().getLocalPort() + " ) lancé");

        InetAddress clientAddress = this.getDatagramPacket().getAddress();
        int clientPort = this.getDatagramPacket().getPort();
        String clientMessage = new String(this.getDatagramPacket().getData());

        System.out.println("Client (" + clientAddress.getHostAddress() + ":" + clientPort + ") - Message : \"" + clientMessage.trim() + "\"");

        DatagramPacket dp = new DatagramPacket(clientMessage.getBytes(), clientMessage.length(), clientAddress, clientPort);

        try {
            System.out.println("Envoi depuis Thread (port: " + this.getDatagramSocket().getLocalPort() + " ) ");
            this.getDatagramSocket().send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                System.out.println("Ecoute du port " + this.getDatagramSocket().getLocalPort());
                this.getDatagramSocket().receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            clientAddress = dp.getAddress();
            clientPort = dp.getPort();
            clientMessage = new String(dp.getData());

            System.out.println("Client (" + clientAddress.getHostAddress() + ":" + clientPort + ") - Message : \"" + clientMessage.trim() + "\"");

            if(clientMessage.contains("!quit")) {
                System.out.println("Fermeture du Thread port " + this.getDatagramSocket().getLocalPort());
                this.getDatagramSocket().close();
                currentThread().interrupt();
                break;
            }

            dp = new DatagramPacket(clientMessage.getBytes(),clientMessage.length(),clientAddress,clientPort);

            try {
                System.out.println("Réponse au client (" + clientAddress.getHostName() + ":" + clientPort +") avec le port " + this.getDatagramSocket().getLocalPort());
                this.getDatagramSocket().send(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
