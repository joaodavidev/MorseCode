import java.util.Scanner;

/**
 * Classe principal que gerencia o menu de interação com o usuário.
 */
public class Main {

    // A árvore é static para que a classe TreeVisualizer possa acessá-la.
    public static MorseTree morseTree;

    public static void main(String[] args) {
        morseTree = new MorseTree();
        populateDefaultMorseTree(); // Pré-carrega a árvore com o alfabeto padrão

        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            displayMenu();
            try {
                // Usamos nextLine() para evitar problemas com o buffer do Scanner
                option = Integer.parseInt(scanner.nextLine()); 

                switch (option) {
                    case 1: handleInsert(scanner); break;
                    case 2: handleEncode(scanner); break;
                    case 3: handleDecode(scanner); break;
                    case 4: handleDisplayTree(args); break;
                    case 0: System.out.println("Encerrando o programa..."); break;
                    default: System.out.println("Opção inválida. Tente novamente."); break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, insira um número válido para a opção.");
            }
            System.out.println(); // Linha em branco para separar os menus
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("--- Menu Código Morse com Árvore Binária ---");
        System.out.println("1. Inserir um novo caractere");
        System.out.println("2. Codificar texto para Morse");
        System.out.println("3. Decodificar Morse para texto");
        System.out.println("4. Exibir a Árvore Gráfica");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void handleInsert(Scanner scanner) {
        System.out.print("Digite o caractere a ser inserido: ");
        String charInput = scanner.nextLine();
        if (charInput.length() != 1) {
            System.out.println("Erro: Insira apenas um único caractere.");
            return;
        }
        char character = charInput.toUpperCase().charAt(0);

        System.out.print("Digite o código Morse correspondente (use '.' e '-'): ");
        String morseCode = scanner.nextLine();

        // Tratamento de erro: verifica se o código contém apenas os caracteres permitidos
        if (!morseCode.matches("[.-]+")) {
            System.out.println("Erro: O código Morse deve conter apenas '.' e '-'.");
            return;
        }

        morseTree.insert(character, morseCode);
        System.out.println("Caractere '" + character + "' inserido com sucesso!");
    }

    private static void handleEncode(Scanner scanner) {
        if (morseTree.isEmpty()) {
            System.out.println("Erro: A árvore está vazia. Não é possível codificar.");
            return;
        }
        System.out.print("Digite o texto para codificar (apenas letras e espaços): ");
        String text = scanner.nextLine();

        if (!text.matches("[a-zA-Z ]+")) {
            System.out.println("Erro: O texto deve conter apenas letras e espaços.");
            return;
        }

        String encodedText = morseTree.encode(text);
        System.out.println("Texto codificado: " + encodedText);
    }

    private static void handleDecode(Scanner scanner) {
        if (morseTree.isEmpty()) {
            System.out.println("Erro: A árvore está vazia. Não é possível decodificar.");
            return;
        }
        System.out.print("Digite o Morse para decodificar (use ' ' entre letras e '/' entre palavras): ");
        String morse = scanner.nextLine();
        
        // Tratamento de erro: verifica se o código contém apenas os caracteres permitidos
        if (!morse.matches("[.\\- /]+")) {
            System.out.println("Erro: Código Morse inválido. Use apenas '.', '-', ' ' e '/'.");
            return;
        }

        // Substitui a barra por um código que será traduzido como espaço
        morse = morse.replace("/", " space ");
        
        String decodedText = morseTree.decode(morse);
        System.out.println("Texto decodificado: " + decodedText.replace("space", " "));
    }
    
    private static void handleDisplayTree(String[] args) {
         if (morseTree.isEmpty()) {
            System.out.println("A árvore está vazia, não há nada para exibir.");
            return;
        }
        System.out.println("Iniciando a interface gráfica da árvore... (Pode demorar um pouco na primeira vez)");
        // Este comando inicia a aplicação JavaFX
        TreeVisualizer.launch(TreeVisualizer.class, args);
    }

    /**
     * Popula a árvore com o alfabeto Morse padrão.
     */
    private static void populateDefaultMorseTree() {
        morseTree.insert('E', "."); morseTree.insert('T', "-");
        morseTree.insert('I', ".."); morseTree.insert('A', ".-");
        morseTree.insert('N', "-."); morseTree.insert('M', "--");
        morseTree.insert('S', "..."); morseTree.insert('U', "..-");
        morseTree.insert('R', ".-."); morseTree.insert('W', ".--");
        morseTree.insert('D', "-.."); morseTree.insert('K', "-.-");
        morseTree.insert('G', "--."); morseTree.insert('O', "---");
        morseTree.insert('H', "...."); morseTree.insert('V', "...-");
        morseTree.insert('F', "..-."); morseTree.insert('L', ".-..");
        morseTree.insert('P', ".--."); morseTree.insert('J', ".---");
        morseTree.insert('B', "-..."); morseTree.insert('X', "-..-");
        morseTree.insert('C', "-.-."); morseTree.insert('Y', "-.--");
        morseTree.insert('Z', "--.."); morseTree.insert('Q', "--.-");
        morseTree.insert('1', ".----"); morseTree.insert('2', "..---");
        morseTree.insert('3', "...--"); morseTree.insert('4', "....-");
        morseTree.insert('5', "....."); morseTree.insert('6', "-....");
        morseTree.insert('7', "--..."); morseTree.insert('8', "---..");
        morseTree.insert('9', "----."); morseTree.insert('0', "-----");
    }
}