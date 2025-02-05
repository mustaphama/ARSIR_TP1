import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Classe principale du serveur pour le chat UDP.
 * Elle écoute les connexions entrantes et dispatche les messages reçus aux autres clients.
 */
public class Server {
    public static void main(String[] args) {
        int portServer = 3501; // Port sur lequel le serveur écoute

        try {
            var pool = Executors.newFixedThreadPool(10);
            Map<Integer, String> portPseudo = new HashMap<>();
            Map<Integer, InetAddress> ipPort = new HashMap<>();

            DatagramSocket ds = new DatagramSocket(portServer);
            System.out.println("Serveur démarré sur le port : " + portServer);

            while (true) {
                DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
                ds.receive(dp);
                Process p = new Process(ds, dp, portPseudo, ipPort);
                pool.execute(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
