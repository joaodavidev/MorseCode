import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// Essa classe aqui faz tudo: a lógica da Árvore Morse e o desenho dela
// na tela usando o Canvas do JavaFX. Deixei as outras classes aqui dentro pra
// ficar tudo num lugar só.
public class VisualizadorArvoreMorse extends Application {

    // Essa é a classe do Nó, basicamente cada "bolinha" da árvore.
    // Coloquei ela aqui dentro pra ficar mais organizado.
    static class NoDaArvore {
        char letra; // A letra que vai dentro da bolinha.
        NoDaArvore filhoEsquerda;   // O filho da esquerda (quando o código tem '.').
        NoDaArvore filhoDireita;  // O filho da direita (quando o código tem '-').

        // Construtor pra criar uma "bolinha" nova já com uma letra.
        public NoDaArvore(char letra) {
            this.letra = letra;
            this.filhoEsquerda = null; // Quando um nó nasce, ele não tem filhos.
            this.filhoDireita = null;
        }
    }

    // Aqui fica toda a inteligência da árvore, tipo inserir, buscar, etc.
    static class ArvoreMorse {
        private NoDaArvore raiz;

        // O construtor. Só cria a árvore com a raiz vazia.
        public ArvoreMorse() {
            // A raiz não é uma letra de verdade, é só o início do caminho.
            // Botei um espaço pra não ficar feio no desenho.
            this.raiz = new NoDaArvore(' ');
        }

        // O método que a gente chama pra começar a inserir uma letra.
        public void inserirLetra(char letra, String codigoMorse) {
            // A mágica mesmo acontece nesse método recursivo aqui de baixo.
            inserirRecursivamente(raiz, letra, codigoMorse);
        }

        // Aqui a recursão acontece de verdade. O método vai se chamando
        // até achar o lugar certo pra por a letra.
        private void inserirRecursivamente(NoDaArvore noAtual, char letra, String restoDoCodigo) {
            // Se o código acabou, opa, achamos o lugar! Coloca a letra aqui.
            if (restoDoCodigo.isEmpty()) {
                noAtual.letra = letra;
                return; // Para a recursão.
            }

            // Pega o primeiro símbolo ('.' ou '-') e o que sobrou do código.
            char direcao = restoDoCodigo.charAt(0);
            String proximoPasso = restoDoCodigo.substring(1);

            // Se for ponto, vamos pra esquerda.
            if (direcao == '.') {
                // Se não tem ninguém na esquerda, cria um nó novo lá.
                if (noAtual.filhoEsquerda == null) {
                    noAtual.filhoEsquerda = new NoDaArvore(' ');
                }
                // E chama a função de novo, mas agora a partir desse filho da esquerda.
                inserirRecursivamente(noAtual.filhoEsquerda, letra, proximoPasso);
            } else if (direcao == '-') { // Se for traço, mesma coisa, mas pra direita.
                if (noAtual.filhoDireita == null) {
                    noAtual.filhoDireita = new NoDaArvore(' ');
                }
                inserirRecursivamente(noAtual.filhoDireita, letra, proximoPasso);
            }
        }

        // Calcula a altura da árvore. Preciso disso pra saber o tamanho da janela.
        public int calcularAltura() {
            return calcularAlturaRecursivamente(raiz);
        }

        // O método que faz o trabalho sujo de calcular a altura de verdade.
        private int calcularAlturaRecursivamente(NoDaArvore no) {
            // Se o nó não existe, a altura dele é zero.
            if (no == null) {
                return 0;
            }
            // A altura é 1 (eu mesmo) mais a altura do meu maior filho.
            return 1 + Math.max(calcularAlturaRecursivamente(no.filhoEsquerda), calcularAlturaRecursivamente(no.filhoDireita));
        }

        // Prepara o "pincel" e manda desenhar a árvore inteira.
        public void desenharArvoreCompleta(Canvas telaDeDesenho) {
            GraphicsContext pincel = telaDeDesenho.getGraphicsContext2D();
            pincel.clearRect(0, 0, telaDeDesenho.getWidth(), telaDeDesenho.getHeight()); // Limpa a tela antes de desenhar.
            pincel.setStroke(Color.BLACK);
            pincel.setLineWidth(1.5);
            pincel.setFont(new Font("Arial", 14));


            // Primeira chamada pra função de desenhar, começando lá no topo.
            desenharNo(pincel, raiz, telaDeDesenho.getWidth() / 2, 40, telaDeDesenho.getWidth() / 4);
        }

        // A função recursiva que desenha cada bolinha e as linhas.
        private void desenharNo(GraphicsContext pincel, NoDaArvore no, double x, double y, double distanciaHorizontalFilho) {
            // Se o nó não existe, não tem o que desenhar.
            if (no == null) {
                return;
            }

            // --- Desenha a bolinha e a letra ---
            pincel.strokeOval(x - 15, y - 15, 30, 30); // Desenha o círculo.
            pincel.fillText(String.valueOf(no.letra), x - 5, y + 5); // Escreve a letra dentro.

            // --- Desenha os filhos (se eles existirem) ---
            if (no.filhoEsquerda != null) {
                double filhoX = x - distanciaHorizontalFilho; // O filho da esquerda fica mais pra esquerda.
                double filhoY = y + 80;      // E um nível abaixo.
                pincel.strokeLine(x - 10, y + 10, filhoX + 10, filhoY - 10); // Linha ligando pai e filho.
                // Agora, manda desenhar o filho da esquerda.
                desenharNo(pincel, no.filhoEsquerda, filhoX, filhoY, distanciaHorizontalFilho / 2);
            }

            if (no.filhoDireita != null) {
                double filhoX = x + distanciaHorizontalFilho; // O filho da direita fica mais pra direita.
                double filhoY = y + 80;
                pincel.strokeLine(x + 10, y + 10, filhoX - 10, filhoY - 10); // Linha de conexão.
                // E manda desenhar o filho da direita.
                desenharNo(pincel, no.filhoDireita, filhoX, filhoY, distanciaHorizontalFilho / 2);
            }
        }
    }

    // Esse é o método 'start', o ponto de partida de qualquer app JavaFX.
    @Override
    public void start(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Visualizador da Árvore Morse");

        // 1. Crio a árvore.
        ArvoreMorse arvore = new ArvoreMorse();

        // 2. Coloco todo o alfabeto Morse dentro dela.
        arvore.inserirLetra('E', "."); arvore.inserirLetra('T', "-");
        arvore.inserirLetra('I', ".."); arvore.inserirLetra('A', ".-");
        arvore.inserirLetra('N', "-."); arvore.inserirLetra('M', "--");
        arvore.inserirLetra('S', "..."); arvore.inserirLetra('U', "..-");
        arvore.inserirLetra('R', ".-."); arvore.inserirLetra('W', ".--");
        arvore.inserirLetra('D', "-.."); arvore.inserirLetra('K', "-.-");
        arvore.inserirLetra('G', "--."); arvore.inserirLetra('O', "---");
        arvore.inserirLetra('H', "...."); arvore.inserirLetra('V', "...-");
        arvore.inserirLetra('F', "..-."); arvore.inserirLetra('L', ".-..");
        arvore.inserirLetra('P', ".--."); arvore.inserirLetra('J', ".---");
        arvore.inserirLetra('B', "-..."); arvore.inserirLetra('X', "-..-");
        arvore.inserirLetra('C', "-.-."); arvore.inserirLetra('Y', "-.--");
        arvore.inserirLetra('Z', "--.."); arvore.inserirLetra('Q', "--.-");
        arvore.inserirLetra('1', ".----"); arvore.inserirLetra('2', "..---");
        arvore.inserirLetra('3', "...--"); arvore.inserirLetra('4', "....-");
        arvore.inserirLetra('5', "....."); arvore.inserirLetra('6', "-....");
        arvore.inserirLetra('7', "--..."); arvore.inserirLetra('8', "---..");
        arvore.inserirLetra('9', "----."); arvore.inserirLetra('0', "-----");


        // 3. Calculo o tamanho que a tela precisa ter pra árvore caber direitinho.
        int alturaDaArvore = arvore.calcularAltura();
        int alturaDaTela = 50 + alturaDaArvore * 80;
        int larguraDaTela = (int) Math.pow(2, alturaDaArvore) * 50;


        // 4. Crio a tela de desenho (o Canvas).
        Canvas telaDeDesenho = new Canvas(Math.max(800, larguraDaTela), alturaDaTela); // Ponho uma largura mínima de 800.
        arvore.desenharArvoreCompleta(telaDeDesenho); // Mando desenhar a árvore na tela.

        // 5. Crio a janela, coloco a tela de desenho dentro e mostro pro usuário.
        Group grupoRaiz = new Group();
        grupoRaiz.getChildren().add(telaDeDesenho);
        Scene cena = new Scene(grupoRaiz);
        palcoPrincipal.setScene(cena);
        palcoPrincipal.show();
    }

    // O 'main' de sempre, só serve pra dar o start na aplicação JavaFX.
    public static void main(String[] args) {
        launch(args);
    }
}

