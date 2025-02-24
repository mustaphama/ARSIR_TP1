package Capitalisation_Q5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCPQ5 {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute du serveur

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur TCP en attente sur le port " + port);

            while (true) {
                // Attente d'une connexion client
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Connexion acceptée : " + clientSocket.getInetAddress());
                    String clientMessage;
                    while ((clientMessage = input.readLine()) != null) {
                        System.out.println("Message reçu du client : " + clientMessage);
                        // Convertir le message en majuscules et renvoyer au client
                        String serverMessage = clientMessage.toUpperCase();
                        output.println(serverMessage);
                    }
                    System.out.println("Connexion fermée avec le client : " + clientSocket.getInetAddress());

                } catch (IOException e) {
                    System.out.println("Erreur lors de la communication avec le client.");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du serveur.");
            e.printStackTrace();
        }
    }
}
