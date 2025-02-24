package Capitalisation_Q5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCPQ5 {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Adresse du serveur
        int serverPort = 12345; // Port du serveur

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connecté au serveur " + serverAddress + " sur le port " + serverPort);
            System.out.println("Envoyez une commande pour obtenir la réponse en majuscules.");

            while (true) {
                System.out.print("Entrez une commande : ");
                String userCommand = scanner.nextLine().trim(); // Lire la commande de l'utilisateur

                // Envoyer la commande au serveur
                output.println(userCommand);

                // Lire la réponse du serveur
                String serverResponse = input.readLine();
                if (serverResponse == null) {
                    System.out.println("Connexion au serveur fermée.");
                    break;
                }

                System.out.println("Réponse du serveur : " + serverResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
