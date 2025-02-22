import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    public static void main(String[] args) throws Exception {
        int port = 12345;
        Jeu jeu = new Jeu();
        System.out.println("Serveur en attente de deux joueurs...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Connexion du premier joueur
            Socket joueur1Socket = serverSocket.accept();
            // Le premier joueur aura pour symbole '1'
            Joueur joueur1 = new Joueur(joueur1Socket, jeu, '1', null);
            
            // Connexion du second joueur
            Socket joueur2Socket = serverSocket.accept();
            // Le second joueur aura pour symbole '2'
            Joueur joueur2 = new Joueur(joueur2Socket, jeu, '2', joueur1);
            
            // On indique que le joueur2 est l'adversaire de joueur1
            joueur1.setAdversaire(joueur2);
            
            // On lance les deux threads
            joueur1.start();
            joueur2.start();
        }
    }
}
