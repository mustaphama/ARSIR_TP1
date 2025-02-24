package horlogeParlante_Q3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientTCP {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Adresse du serveur
        int serverPort = 12345; // Port du serveur

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connecté au serveur " + serverAddress + " sur le port " + serverPort);

            // Lire l'heure envoyée par le serveur
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedTime = input.readLine();

            System.out.println("Heure reçue du serveur : " + receivedTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
