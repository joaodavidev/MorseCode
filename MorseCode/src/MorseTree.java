/**
 * Implementa a Árvore Binária de Busca para Código Morse.
 * A inserção e a busca seguem a regra: '.' para a esquerda, '-' para a direita.
 * Todas as operações principais são implementadas de forma recursiva.
 */
public class MorseTree {

    private Node root;

    /**
     * Construtor da árvore. Inicializa a raiz. [cite: 89]
     * A raiz começa com um caractere nulo, pois ela é apenas o ponto de partida.
     */
    public MorseTree() {
        this.root = new Node('\0');
    }

    public Node getRoot() {
        return this.root;
    }

    public boolean isEmpty() {
        // A árvore é considerada vazia se a raiz não tem filhos.
        return root.left == null && root.right == null;
    }

    // --- INSERÇÃO RECURSIVA ---

    /**
     * Método público para iniciar a inserção de um caractere.
     * @param character O caractere a ser inserido (ex: 'A').
     * @param morseCode O código Morse correspondente (ex: ".-").
     */
    public void insert(char character, String morseCode) {
        // A recursão sempre começa a partir do nó raiz. [cite: 280]
        insertRecursive(root, character, morseCode);
    }

    /**
     * Método privado e recursivo que navega na árvore para inserir o caractere.
     * @param current O nó atual na recursão.
     * @param character O caractere a ser inserido.
     * @param morseCode A parte restante do código Morse a ser seguida.
     */
    private void insertRecursive(Node current, char character, String morseCode) {
        // Caso base: Se o código Morse acabou, encontramos a posição.
        if (morseCode.isEmpty()) {
            current.character = character;
            return;
        }

        char direction = morseCode.charAt(0);
        String remainingCode = morseCode.substring(1);

        if (direction == '.') {
            if (current.left == null) {
                // Se o caminho não existe, cria um novo nó (inicialmente sem caractere).
                current.left = new Node('\0');
            }
            insertRecursive(current.left, character, remainingCode);
        } else if (direction == '-') {
            if (current.right == null) {
                current.right = new Node('\0');
            }
            insertRecursive(current.right, character, remainingCode);
        }
    }

    // --- DECODIFICAÇÃO RECURSIVA ---

    /**
     * Decodifica uma palavra completa em código Morse.
     * @param morseText Uma string de códigos Morse separados por espaço.
     * @return A palavra decodificada.
     */
    public String decode(String morseText) {
        StringBuilder decodedWord = new StringBuilder();
        String[] letters = morseText.trim().split(" ");

        for (String morseLetter : letters) {
            char decodedChar = decodeRecursive(root, morseLetter);
            // Se o código for inválido, o método retorna um caractere nulo.
            decodedWord.append(decodedChar == '\0' ? '?' : decodedChar);
        }
        return decodedWord.toString();
    }

    /**
     * Método privado recursivo que busca um caractere a partir do seu código Morse.
     * @param current O nó atual na recursão.
     * @param morseCode A parte restante do código a ser seguida.
     * @return O caractere encontrado, ou '\0' se o caminho não existir.
     */
    private char decodeRecursive(Node current, String morseCode) {
        // Se chegamos a um nó nulo, o código é inválido.
        if (current == null) {
            return '\0';
        }
        // Caso base: Se o código acabou, retornamos o caractere do nó atual.
        if (morseCode.isEmpty()) {
            return current.character;
        }

        char direction = morseCode.charAt(0);
        String remainingCode = morseCode.substring(1);

        if (direction == '.') {
            return decodeRecursive(current.left, remainingCode);
        } else { // Assume que é '-'
            return decodeRecursive(current.right, remainingCode);
        }
    }

    // --- CODIFICAÇÃO (COM BUSCA RECURSIVA) ---

    /**
     * Codifica um texto para código Morse.
     * @param plainText O texto a ser codificado (ex: "JAVA").
     * @return A representação em código Morse.
     */
    public String encode(String plainText) {
        StringBuilder morseResult = new StringBuilder();
        plainText = plainText.toUpperCase();

        for (char character : plainText.toCharArray()) {
            if (character == ' ') {
                 morseResult.append("/ ");
                 continue;
            }
            // Para cada caractere, faz uma busca recursiva na árvore.
            String morseCode = findMorseCodeRecursive(root, character, "");
            if (morseCode != null) {
                morseResult.append(morseCode).append(" ");
            } else {
                morseResult.append("? "); // Caractere não encontrado
            }
        }
        return morseResult.toString().trim();
    }

    /**
     * Método privado recursivo que faz uma busca para encontrar o código Morse de um caractere.
     * @param current O nó atual na recursão.
     * @param targetChar O caractere que estamos procurando.
     * @param currentPath O caminho percorrido até agora (ex: ".-").
     * @return A string com o código Morse, ou null se não for encontrado.
     */
    private String findMorseCodeRecursive(Node current, char targetChar, String currentPath) {
        // Caso base: Se o nó é nulo, o caractere não está neste galho.
        if (current == null) {
            return null;
        }
        // Se encontramos o caractere, retornamos o caminho que nos trouxe até aqui.
        if (current.character == targetChar) {
            return currentPath;
        }

        // Procura na subárvore esquerda (adicionando '.')
        String foundPath = findMorseCodeRecursive(current.left, targetChar, currentPath + ".");
        if (foundPath != null) {
            return foundPath;
        }

        // Se não achou na esquerda, procura na direita (adicionando '-')
        return findMorseCodeRecursive(current.right, targetChar, currentPath + "-");
    }
}