import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try (DatagramSocket socket = new DatagramSocket()) {
            // Heure d'envoi T₁
            long T1 = System.currentTimeMillis();
            byte[] requestData = String.valueOf(T1).getBytes();

            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            DatagramPacket request = new DatagramPacket(requestData, requestData.length, serverInetAddress, serverPort);
            socket.send(request);
            System.out.println("Requête envoyée au serveur avec T₁ = " + T1);

            // Réception de la réponse du serveur
            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            // Heure de réception T₂
            long T2 = System.currentTimeMillis();

            // Extraire T₁, T'₁ et T'₂ du message reçu
            String receivedData = new String(response.getData(), 0, response.getLength());
            String[] timestamps = receivedData.split(",");
            long T1Received = Long.parseLong(timestamps[0]);
            long T1Prime = Long.parseLong(timestamps[1]);
            long T2Prime = Long.parseLong(timestamps[2]);

            // Calcul du délai de transmission δ
            long delta = (T2 - T1) - (T2Prime - T1Prime);

            // Calcul du décalage des horloges θ
            long theta = ((T1Prime + T2Prime) / 2) - ((T1 + T2) / 2);

            System.out.println("Réponse reçue : T₁ = " + T1Received + ", T'₁ = " + T1Prime + ", T'₂ = " + T2Prime);
            System.out.println("T₂ = " + T2);
            System.out.println("Délai de transmission δ = " + delta + " ms");
            System.out.println("Décalage des horloges θ = " + theta + " ms");

            // Ajuster l'horloge du client si besoin
            System.out.println("Nouvelle horloge client = " + (T2 + theta) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




// UDP est utilisé car il est plus rapide que TCP, mais il n'offre pas de garantie de livraison des paquets.