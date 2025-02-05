package UDP_Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Ce processus client écoute et reçoit les messages du serveur.
 * Il est conçu pour être exécuté dans un thread séparé pour permettre une écoute continue sans bloquer l'envoi de messages.
 */
public class ClientReceiveProcess implements Runnable {
    private DatagramSocket ds;

    /**
     * Constructeur pour ClientReceiveProcess.
     * @param ds Le socket Datagram utilisé pour recevoir les données.
     */
    public ClientReceiveProcess(DatagramSocket ds) {
        this.ds = ds;
    }

    /**
     * Méthode run qui écoute les messages venant du serveur et les affiche à l'utilisateur.
     */
    public void run() {
        while (true) {
            try {
                DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
                ds.receive(dp);
                System.out.println(new String(dp.getData(), 0, dp.getLength()));
                System.out.print("Enter a message:\n ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
