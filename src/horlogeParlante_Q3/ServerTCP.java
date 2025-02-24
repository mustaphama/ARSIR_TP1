package horlogeParlante_Q3;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerTCP {
    public static void main(String[] args) {
        int port = 12345; // Port d'écoute du serveur

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur TCP en attente sur le port " + port);

            while (true) {
                // Attente d'une connexion client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion acceptée : " + clientSocket.getInetAddress());

                // Obtenir l'heure actuelle
                String T1Server = new SimpleDateFormat("HH:mm:ss").format(new Date());

                // Envoyer l'heure au client
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                output.println(T1Server);
                System.out.println("Heure envoyée au client : " + T1Server);

                // Fermer la connexion
                clientSocket.close();
                System.out.println("Connexion fermée.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
