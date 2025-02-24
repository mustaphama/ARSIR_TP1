package horlogeParlante_Q1;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try (DatagramSocket socket = new DatagramSocket()) {
            // Heure d'envoi T₁
            String requete = "";
            byte[] requestData = requete.getBytes();

            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            DatagramPacket request = new DatagramPacket(requestData, requestData.length, serverInetAddress, serverPort);
            socket.send(request);
            System.out.println("Requête envoyée au serveur");

            // Réception de la réponse du serveur
            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            String receivedData = new String(response.getData(), 0, response.getLength());

            // Ajuster l'horloge du client si besoin
            System.out.println("Réponse reçue du serveur :" + receivedData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}