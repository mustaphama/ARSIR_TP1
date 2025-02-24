package horlogeParlante_Q2;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                long T1 = System.currentTimeMillis();

                // Récupération de l'adresse et du port du client
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                String responseData = ( new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(T1)));
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
