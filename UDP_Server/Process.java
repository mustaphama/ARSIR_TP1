import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

/**
 * Cette classe gère le traitement des paquets de données reçus par le serveur.
 * Elle implémente l'interface Runnable pour permettre son exécution dans un thread séparé.
 */
public class Process implements Runnable {
    private DatagramSocket ds;
    private Map<Integer, String> sockets;
    private DatagramPacket dp;
    private Map<Integer,InetAddress> ipPort;

    /**
     * Constructeur de la classe Process.
     * @param ds Socket Datagram pour envoyer et recevoir les paquets.
     * @param dp Le paquet Datagram reçu.
     * @param sockets Map associant les ports aux pseudonymes des clients.
     * @param ipPort Map associant les ports aux adresses IP des clients.
     */
    public Process(DatagramSocket ds, DatagramPacket dp, Map<Integer, String> sockets, Map<Integer,InetAddress> ipPort) {
        this.ds = ds;
        this.sockets = sockets;
        this.dp = dp;
        this.ipPort = ipPort;
    }

    /**
     * Méthode run qui est appelée lorsque le thread est démarré.
     * Elle gère la réception des messages, l'enregistrement de nouveaux clients, et la redirection des messages privés ou publics.
     */
    public void run() {
        // Récupérer le texte du message envoyé
        String message = new String(dp.getData(), 0, dp.getLength());
        System.out.println("[Server] Message reçu : " + message + " par " + dp.getAddress() + " "
                + sockets.get(dp.getPort()) + " " + dp.getPort());

        // Ajouter le client à la liste s'il n'y est pas encore
        if (!sockets.containsKey(dp.getPort())) {
            sockets.put(dp.getPort(), message);
            ipPort.put(dp.getPort(), dp.getAddress());
            System.out.println("[Server] Connexion établie : " + dp.getAddress() + " " + dp.getPort());
        } else {
            if (message.contains("/")) {
                messagePrivé(dp);
            } else {
                messageCommun(dp);
            }
        }
    }

    /**
     * Envoie un message privé à un utilisateur spécifié.
     * @param dp Le paquet contenant le message et le destinataire.
     */
    public void messagePrivé(DatagramPacket dp) {
        Boolean pseudoExiste = false;
        String message = new String(dp.getData(), 0, dp.getLength());
        String[] parts = message.split("/");
        String messagePort = "MP : [" + sockets.get(dp.getPort()) + "]: " + parts[1];
        byte[] buffer = messagePort.getBytes();
        String pseudo = parts[0];

        for (int socket : sockets.keySet()) {
            try {
                if (pseudo.equals(sockets.get(socket))) {
                    pseudoExiste = true;
                    DatagramPacket send_dp = new DatagramPacket(buffer, buffer.length, ipPort.get(socket), socket);
                    ds.send(send_dp);
                    System.out.println("[Server] Envoi : " + parts[1] + " à " + ipPort.get(socket) + " "
                            + sockets.get(socket) + " " + socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!pseudoExiste) {
            System.out.println("[Server] Pseudo inexistant");
        }
    }

    /**
     * Envoie un message à tous les clients sauf à celui qui l'a envoyé.
     * @param dp Le paquet contenant le message à diffuser.
     */
    public void messageCommun(DatagramPacket dp) {
        String message = new String(dp.getData(), 0, dp.getLength());
        String messagePort = "[" + sockets.get(dp.getPort()) + "]: " + message;
        byte[] buffer = messagePort.getBytes();

        for (int socket : sockets.keySet()) {
            try {
                if (socket != dp.getPort()) {
                    DatagramPacket send_dp = new DatagramPacket(buffer, buffer.length, ipPort.get(socket), socket);
                    ds.send(send_dp);
                    System.out.println("[Server] Envoi : " + message + " à " + ipPort.get(socket) + " " + sockets.get(socket)
                            + " " + socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
