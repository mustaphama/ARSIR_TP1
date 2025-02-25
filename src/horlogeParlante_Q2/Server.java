package horlogeParlante_Q2;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute du serveur

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Serveur UDP en attente sur le port " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                // Réception du message du client
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                
                // Heure de réception T'₁ en `long`
                long T1Prime = System.currentTimeMillis();

                // Récupération de l'adresse et du port du client
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                // Préparer la réponse avec un timestamp `long`
                String responseData = String.valueOf(T1Prime);
                byte[] responseBytes = responseData.getBytes();

                // Envoi de la réponse au client
                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, clientPort);
                socket.send(response);

                System.out.println("Réponse envoyée au client : " + T1Prime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
