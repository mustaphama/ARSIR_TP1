package puissance4;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 12345;
        try (Socket socket = new Socket(host, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println(in.readLine()); // Ex: "WELCOME 1" ou "WELCOME 2"
            System.out.println(in.readLine()); // Ex: "GAME START"

            while (true) {
                String serverResponse = in.readLine();
                if (serverResponse == null) {
                    System.out.println("Le serveur a fermé la connexion.");
                    break;
                }
                
                switch (serverResponse) {
                    case "WAIT":
                        System.out.println("Ce n'est pas votre tour. Patientez...");
                        Thread.sleep(500);
                        // Ne rien demander ici
                        break;
                        
                    case "READY":
                        System.out.println("C'est votre tour !");
                        System.out.print("Entrez une colonne (0-6) : ");
                        int col = scanner.nextInt();
                        out.println("PLAY " + col);
                        break;
                        
                    case "VICTORY":
                        System.out.println("Vous avez gagné !");
                        return;
                        
                    case "DEFEAT":
                        System.out.println("Vous avez perdu.");
                        return;
                    default:
                        // Si le message commence par "OPPONENT MOVE"
                        if (serverResponse.startsWith("OPPONENT MOVE")) {
                            String move = serverResponse.split(" ")[2];
                            System.out.println("L'adversaire a joué : " + move);
                        }
                        // Si le message commence par "VALID MOVE"
                        else if (serverResponse.startsWith("VALID MOVE")) {
                            String move = serverResponse.split(" ")[2];
                            System.out.println("Votre coup " + move + " est validé.");
                        }
                        // Si jamais il y a d'autres messages
                        else {
                            System.out.println(serverResponse);
                        }
                        break;
                }
            }
            
        }
    }
}
