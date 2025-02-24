package puissance4;
public class Jeu {
    private final int rows = 6; // Nombre de lignes
    private final int cols = 7; // Nombre de colonnes
    private final char[][] plateau; // Plateau de jeu
    private char currentPlayer;     // '1' commencera

    public Jeu() {
        plateau = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                plateau[i][j] = ' '; // Initialise le plateau vide
            }
        }
        // Le joueur '1' commence par défaut
        currentPlayer = '1';
    }

    public synchronized boolean jouerCoup(int col) {
        // On vérifie si la colonne est valide
        if (col < 0 || col >= cols) return false;

        // On place le pion au plus bas dans la colonne
        for (int i = rows - 1; i >= 0; i--) {
            if (plateau[i][col] == ' ') {
                plateau[i][col] = currentPlayer; // Place le jeton
                // Bascule le joueur actuel :
                // si c'était '1', devient '2', sinon redevient '1'
                currentPlayer = (currentPlayer == '1') ? '2' : '1';
                return true;
            }
        }
        // Si la colonne est pleine, on retourne false
        return false;
    }

    public boolean verifierVictoire() {
        // Vérifie les lignes, colonnes et diagonales
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char player = plateau[i][j];
                if (player != ' ' && (
                        checkDirection(i, j, 1, 0, player) || // Horizontal
                        checkDirection(i, j, 0, 1, player) || // Vertical
                        checkDirection(i, j, 1, 1, player) || // Diagonale \
                        checkDirection(i, j, 1, -1, player)   // Diagonale /
                )) {
                    return true; // Victoire trouvée
                }
            }
        }
        return false;
    }

    // Méthode utilitaire de vérification
    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        for (int i = 0; i < 4; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
            // Si on sort du plateau ou que ça ne correspond pas au player, on annule
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols 
                || plateau[newRow][newCol] != player) {
                return false;
            }
        }
        return true;
    }

    // Retourne le joueur qui doit jouer maintenant
    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    // Permet de forcer le joueur courant (utilisé par Joueur.java quand on passe le tour)
    public void setCurrentPlayer(char player) {
        currentPlayer = player;
        System.out.println("Le joueur actuel est maintenant : " + currentPlayer);
    }

    public synchronized char[][] getPlateau() {
        return plateau;
    }
}
