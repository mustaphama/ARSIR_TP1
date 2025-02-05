package UDP_Client;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.net.DatagramPacket; 
import java.net.InetAddress;

/**
 * Classe principale du client pour le chat UDP.
 * Elle configure la connexion au serveur et permet l'envoi et la réception de messages.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String ipServer="192.168.208.1"; // IP du serveur local (à changer si le serveur est distant). 
        //Si jamais remplacer InetAddress.getByName(ipServer) par InetAddress.getLocalHost() aux lignes 33 et 44
        int portServer = 3501;

        try {
            System.out.println("\nEntrez votre nom :");
            var name = sc.nextLine();
            byte[] buf = name.getBytes();

            DatagramSocket ds = new DatagramSocket();
            DatagramPacket dp;
            ExecutorService pool = Executors.newFixedThreadPool(10);
            ClientReceiveProcess proc = new ClientReceiveProcess(ds);

            dp = new DatagramPacket(buf, buf.length,InetAddress.getByName(ipServer), portServer);
            System.out.println("Connexion au serveur...");
            ds.send(dp);
            System.out.println("Connecté au serveur ! ");
            pool.execute(proc);

            while (true) {
                System.out.print("Enter a message:\n");
                String message = sc.nextLine();
                byte[] buffer = message.getBytes();

                dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipServer), portServer);
                ds.send(dp);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
