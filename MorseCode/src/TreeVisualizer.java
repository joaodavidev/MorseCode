import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe única que contém a lógica da Árvore Morse e sua visualização gráfica
 * usando JavaFX Canvas. A estrutura da árvore é implementada através de classes
 * aninhadas (nested classes).
 */
public class TreeVisualizer extends Application {

    // --- CLASSE ANINHADA PARA O NÓ DA ÁRVORE ---
    // Esta classe é a mesma que o nosso antigo Node.java, mas agora vive dentro de TreeVisualizer.
    // O nome da variável 'character' foi alterado para 'letter' para corresponder ao método de desenho.
    static class Node {
        char letter; // O caractere (ex: 'A', 'B').
        Node left;   // Filho da esquerda (caminho do '.').
        Node right;  // Filho da direita (caminho do '-').

        public Node(char letter) {
            this.letter = letter;
            this.left = null;
            this.right = null;
        }
    }

    // --- CLASSE ANINHADA PARA A LÓGICA DA ÁRVORE ---
    // Esta classe contém toda a lógica que estava no nosso antigo MorseTree.java.
    static class MorseBST {
        private Node root;

        /**
         * Construtor. Inicializa a árvore com um nó raiz vazio.
         */
        public MorseBST() {
            // A raiz não representa uma letra, é apenas o ponto de partida.
            // Usamos um espaço em branco para não poluir a visualização.
            this.root = new Node(' ');
        }

        /**
         * Método público para iniciar a inserção recursiva.
         * @param letter O caractere a ser inserido.
         * @param morseCode O código Morse correspondente.
         */
        public void insert(char letter, String morseCode) {
            insertRecursive(root, letter, morseCode);
        }

        /**
         * Lógica de inserção recursiva: ponto (.) para a esquerda e traço (-) para a direita.
         * Esta é a mesma lógica do nosso arquivo MorseTree.java.
         */
        private void insertRecursive(Node current, char letter, String morseCode) {
            if (morseCode.isEmpty()) {
                current.letter = letter;
                return;
            }

            char direction = morseCode.charAt(0);
            String remainingCode = morseCode.substring(1);

            if (direction == '.') {
                if (current.left == null) {
                    current.left = new Node(' ');
                }
                insertRecursive(current.left, letter, remainingCode);
            } else if (direction == '-') {
                if (current.right == null) {
                    current.right = new Node(' ');
                }
                insertRecursive(current.right, letter, remainingCode);
            }
        }

        // Os métodos de codificar e decodificar poderiam ser adicionados aqui da mesma forma.
        // Por simplicidade, eles foram omitidos, já que esta classe foca na visualização.

        /**
         * Calcula a altura da árvore para ajustar o tamanho da janela.
         */
        public int getHeight() {
            return getHeight(root);
        }

        private int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            // A altura é 1 (o nó atual) + a altura da maior sub-árvore (esquerda ou direita).
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        /**
         * Inicia o processo de desenho da árvore no Canvas.
         * @param canvas O componente JavaFX onde o desenho será feito.
         */
        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Limpa a tela
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.5);
            gc.setFont(new Font("Arial", 14));


            // A chamada inicial para o método de desenho recursivo.
            // O desenho começa no centro superior da tela.
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4);
        }

        /**
         * Método recursivo que desenha cada nó e as linhas que os conectam.
         * @param gc O "pincel" para desenhar no canvas.
         * @param node O nó atual a ser desenhado.
         * @param x A coordenada X do centro do nó.
         * @param y A coordenada Y do centro do nó.
         * @param xOffset O deslocamento horizontal para os filhos. Diminui a cada nível.
         */
        private void drawNode(GraphicsContext gc, Node node, double x, double y, double xOffset) {
            if (node == null) {
                return;
            }

            // --- Desenha o nó atual ---
            // Desenha um círculo para o nó.
            gc.strokeOval(x - 15, y - 15, 30, 30);

            // Desenha a letra dentro do círculo.
            gc.fillText(String.valueOf(node.letter), x - 5, y + 5);

            // --- Desenha os filhos e as linhas de conexão ---
            if (node.left != null) {
                double childX = x - xOffset;
                double childY = y + 80; // Espaçamento vertical fixo entre os níveis.
                gc.strokeLine(x - 10, y + 10, childX + 10, childY - 10); // Linha de conexão
                // Chamada recursiva para desenhar o filho da esquerda.
                drawNode(gc, node.left, childX, childY, xOffset / 2);
            }

            if (node.right != null) {
                double childX = x + xOffset;
                double childY = y + 80;
                gc.strokeLine(x + 10, y + 10, childX - 10, childY - 10); // Linha de conexão
                // Chamada recursiva para desenhar o filho da direita.
                drawNode(gc, node.right, childX, childY, xOffset / 2);
            }
        }
    }

    /**
     * Método principal da aplicação JavaFX. É aqui que a janela é configurada.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visualizador da Árvore Morse");

        // 1. Cria uma instância da nossa árvore Morse.
        MorseBST bst = new MorseBST();

        // 2. Popula a árvore com o alfabeto Morse.
        // Esta é a mesma lógica do método 'populateDefaultMorseTree' do Main.java anterior.
        bst.insert('E', "."); bst.insert('T', "-");
        bst.insert('I', ".."); bst.insert('A', ".-");
        bst.insert('N', "-."); bst.insert('M', "--");
        bst.insert('S', "..."); bst.insert('U', "..-");
        bst.insert('R', ".-."); bst.insert('W', ".--");
        bst.insert('D', "-.."); bst.insert('K', "-.-");
        bst.insert('G', "--."); bst.insert('O', "---");
        bst.insert('H', "...."); bst.insert('V', "...-");
        bst.insert('F', "..-."); bst.insert('L', ".-..");
        bst.insert('P', ".--."); bst.insert('J', ".---");
        bst.insert('B', "-..."); bst.insert('X', "-..-");
        bst.insert('C', "-.-."); bst.insert('Y', "-.--");
        bst.insert('Z', "--.."); bst.insert('Q', "--.-");
        bst.insert('1', ".----"); bst.insert('2', "..---");
        bst.insert('3', "...--"); bst.insert('4', "....-");
        bst.insert('5', "....."); bst.insert('6', "-....");
        bst.insert('7', "--..."); bst.insert('8', "---..");
        bst.insert('9', "----."); bst.insert('0', "-----");


        // 3. Calcula o tamanho ideal da janela com base na altura e largura da árvore.
        int height = bst.getHeight();
        // A altura da tela é proporcional à altura da árvore.
        int canvasHeight = 50 + height * 80;
        // A largura da tela é proporcional à quantidade de nós no último nível.
        int canvasWidth = (int) Math.pow(2, height) * 50;


        // 4. Cria o Canvas (a tela de desenho) com o tamanho calculado.
        Canvas canvas = new Canvas(Math.max(800, canvasWidth), canvasHeight); // Garante uma largura mínima
        bst.drawTree(canvas); // Chama o método para desenhar a árvore no canvas.

        // 5. Configura e exibe a cena (a janela).
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Ponto de entrada do programa. Apenas inicia a aplicação JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
