import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Joueur extends Thread {
    private final Socket socket;
    private final Jeu jeu;
    private final char symbole;        // '1' ou '2'
    private Joueur adversaire;
    
    private BufferedReader in;
    private PrintWriter out;
    
    public Joueur(Socket socket, Jeu jeu, char symbole, Joueur adversaire) throws IOException {
        this.socket = socket;
        this.jeu = jeu;
        this.symbole = symbole;       // On fixe '1' ou '2'
        this.adversaire = adversaire;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void setAdversaire(Joueur adversaire) {
        this.adversaire = adversaire;
    }
    
    @Override
    public void run() {
        try {
            out.println("WELCOME " + symbole);
            System.out.println("Joueur " + symbole + " connecté.");
            out.println("GAME START");

            // Le joueur '1' commence, donc on lui envoie "READY"
            if (symbole == '1') {
                out.println("READY"); 
            }

            while (true) {
                synchronized (jeu) {
                    // Si ce n'est pas le tour de ce joueur, on lui envoie "WAIT" et on attend
                    if (jeu.getCurrentPlayer() != symbole) {
                        out.println("WAIT");
                        System.out.println("Joueur " + symbole + " attend son tour...");
                        jeu.wait(); 
                        continue;
                    }

                    // C'est le tour de ce joueur
                    System.out.println("En attente d'une commande du joueur " + symbole + "...");

                    String input = in.readLine();
                    
                    // Si input est null => le client a coupé la connexion
                    if (input == null) {
                        System.out.println("Le joueur " + symbole + " s'est déconnecté.");
                        if (adversaire != null) {
                            adversaire.sendMessage("OPPONENT LEFT");
                        }
                        break;
                    }

                    System.out.println("Commande reçue de " + symbole + ": " + input);

                    if (input.startsWith("PLAY")) {
                        int col = Integer.parseInt(input.split(" ")[1]);
                        
                        if (jeu.jouerCoup(col)) {
                            out.println("VALID MOVE " + col);
                            System.out.println("Coup valide joué par le Joueur " + symbole 
                                               + " en colonne " + col);
                            
                            adversaire.sendMessage("OPPONENT MOVE " + col);

                            // Vérifier si ce coup mène à la victoire
                            if (jeu.verifierVictoire()) {
                                out.println("VICTORY");
                                adversaire.sendMessage("DEFEAT");
                                break; // Partie terminée
                            }

                            // Passe au joueur suivant
                            System.out.println("Le joueur actuel est maintenant : " 
                                               + adversaire.symbole);

                            // Notifie le joueur suivant qu'il peut jouer
                            adversaire.sendMessage("READY");

                            // On réveille l'autre joueur
                            jeu.notifyAll();
                        } else {
                            out.println("INVALID MOVE");
                            System.out.println("Coup invalide de " + symbole 
                                               + " en colonne " + col);
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erreur ou déconnexion du joueur " + symbole + ": " + e.getMessage());
        }
    }
    
    public void sendMessage(String message) {
        out.println(message);
    }
}
