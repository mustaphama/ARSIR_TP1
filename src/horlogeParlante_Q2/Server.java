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

                // Heure de réception T'₁
                long T1Prime = System.currentTimeMillis();

                // Récupération de l'adresse et du port du client
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                // Extraction de T₁ envoyé par le client
                String receivedData = new String(request.getData(), 0, request.getLength());
                long T1 = Long.parseLong(receivedData);

                // Heure d'envoi T'₂
                long T2Prime = System.currentTimeMillis();

                // Construire la réponse contenant T₁, T'₁ et T'₂
                String responseData = T1 + "," + T1Prime + "," + T2Prime;
                byte[] responseBytes = responseData.getBytes();

                // Envoi de la réponse au client
                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length, clientAddress, clientPort);
                socket.send(response);

                System.out.println("Réponse envoyée au client : " + responseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}