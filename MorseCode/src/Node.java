/**
 * Representa um único nó na Árvore Binária de Código Morse. 
 * Cada nó contém uma informação (o caractere) e referências para os nós filhos. [cite: 66, 75]
 */
public class Node {
    char character; // O caractere (ex: 'A', 'B', 'C') que o nó armazena.
    Node left;      // Filho da esquerda (caminho do '.').
    Node right;     // Filho da direita (caminho do '-').

    /**
     * Construtor para criar um novo nó com um caractere.
     * @param character O caractere a ser armazenado no nó.
     */
    public Node(char character) {
        this.character = character;
        this.left = null;
        this.right = null;
    }
}