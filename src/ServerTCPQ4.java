import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTCPQ4 {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute du serveur

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur TCP en attente sur le port " + port);
            while (true) {
                // Attente d'une connexion client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion acceptée : " + clientSocket.getInetAddress());

                // Création des flux de communication
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                String clientMessage;
                while ((clientMessage = input.readLine()) != null) {
                    System.out.println("Message reçu du client : " + clientMessage);
                    if (clientMessage.equals("DATE")) {
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        output.println("DATE: " + currentDate);
                    } else if (clientMessage.equalsIgnoreCase("HOUR")) {
                        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                        output.println("HOUR: " + currentTime);
                    } else if (clientMessage.equalsIgnoreCase("FULL")) {
                        String fullDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        output.println("FULL: " + fullDateTime);
                    } else if (clientMessage.equalsIgnoreCase("CLOSE")) {
                        System.out.println("Fermeture de la connexion demandée par le client.");
                        output.println("Connexion fermée par le serveur.");
                        break;
                    } else {
                        output.println("Commande invalide. Envoyez DATE, HOUR, FULL ou CLOSE.");
                    }
                }
                // Fermer la connexion avec le client après réception de "CLOSE"
                clientSocket.close();
                System.out.println("Connexion fermée.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
