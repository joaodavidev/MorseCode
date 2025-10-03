import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Scanner;

// Beleza, essa classe aqui faz tudo: a lógica da Árvore Morse, o desenho dela
// e agora também o menu de texto pra interagir com o usuário.
public class TreeVisualizer extends Application {

    // Criei uma árvore estática. Isso é importante pra que o menu de texto
    // e a janela gráfica consigam "conversar" e usar a MESMA árvore.
    private static ArvoreMorse arvore;

    // Essa é a classe do Nó, basicamente cada "bolinha" da árvore.
    static class NoDaArvore {
        char letra;
        NoDaArvore filhoEsquerda;
        NoDaArvore filhoDireita;

        public NoDaArvore(char letra) {
            this.letra = letra;
            this.filhoEsquerda = null;
            this.filhoDireita = null;
        }
    }

    // Aqui fica toda a inteligência da árvore, tipo inserir, buscar, etc.
    static class ArvoreMorse {
        private NoDaArvore raiz;

        // O construtor. Só cria a árvore com a raiz vazia.
        public ArvoreMorse() {
            this.raiz = new NoDaArvore(' ');
        }

        // Checa se a árvore está vazia (só com a raiz).
        public boolean estaVazia() {
            return raiz.filhoEsquerda == null && raiz.filhoDireita == null;
        }

        // O método que a gente chama pra começar a inserir uma letra.
        public void inserirLetra(char letra, String codigoMorse) {
            inserirRecursivamente(raiz, letra, codigoMorse);
        }

        private void inserirRecursivamente(NoDaArvore noAtual, char letra, String restoDoCodigo) {
            if (restoDoCodigo.isEmpty()) {
                noAtual.letra = letra;
                return;
            }
            char direcao = restoDoCodigo.charAt(0);
            String proximoPasso = restoDoCodigo.substring(1);
            if (direcao == '.') {
                if (noAtual.filhoEsquerda == null) {
                    noAtual.filhoEsquerda = new NoDaArvore(' ');
                }
                inserirRecursivamente(noAtual.filhoEsquerda, letra, proximoPasso);
            } else if (direcao == '-') {
                if (noAtual.filhoDireita == null) {
                    noAtual.filhoDireita = new NoDaArvore(' ');
                }
                inserirRecursivamente(noAtual.filhoDireita, letra, proximoPasso);
            }
        }

        // Decodifica uma frase inteira em morse.
        public String decodificarMorse(String textoMorse) {
            StringBuilder palavraDecodificada = new StringBuilder();
            String[] codigosLetras = textoMorse.trim().split(" ");

            for (String codigoLetra : codigosLetras) {
                char letraEncontrada = buscarLetraPeloCodigo(raiz, codigoLetra);
                palavraDecodificada.append(letraEncontrada == '\0' ? '?' : letraEncontrada);
            }
            return palavraDecodificada.toString();
        }

        // A função recursiva que desce na árvore pra achar a letra.
        private char buscarLetraPeloCodigo(NoDaArvore noAtual, String restoDoCodigo) {
            if (noAtual == null) {
                return '\0'; // Caminho inválido.
            }
            if (restoDoCodigo.isEmpty()) {
                return noAtual.letra;
            }
            char direcao = restoDoCodigo.charAt(0);
            String proximoPasso = restoDoCodigo.substring(1);
            if (direcao == '.') {
                return buscarLetraPeloCodigo(noAtual.filhoEsquerda, proximoPasso);
            } else {
                return buscarLetraPeloCodigo(noAtual.filhoDireita, proximoPasso);
            }
        }

        // Codifica um texto normal para morse.
        public String codificarParaMorse(String textoNormal) {
            StringBuilder resultadoMorse = new StringBuilder();
            textoNormal = textoNormal.toUpperCase();
            for (char letra : textoNormal.toCharArray()) {
                if (letra == ' ') {
                    resultadoMorse.append("/ ");
                    continue;
                }
                // Pra cada letra, a gente busca o caminho (código) dela na árvore.
                String codigoEncontrado = buscarCodigoPelaLetra(raiz, letra, "");
                if (codigoEncontrado != null) {
                    resultadoMorse.append(codigoEncontrado).append(" ");
                } else {
                    resultadoMorse.append("? ");
                }
            }
            return resultadoMorse.toString().trim();
        }

        // A função recursiva que "caça" a letra na árvore pra descobrir o caminho.
        private String buscarCodigoPelaLetra(NoDaArvore noAtual, char letraAlvo, String caminhoAteAqui) {
            if (noAtual == null) {
                return null; // Não achei nesse galho.
            }
            if (noAtual.letra == letraAlvo) {
                return caminhoAteAqui; // Achei! Retorna o caminho.
            }
            String caminhoEncontrado = buscarCodigoPelaLetra(noAtual.filhoEsquerda, letraAlvo, caminhoAteAqui + ".");
            if (caminhoEncontrado != null) {
                return caminhoEncontrado;
            }
            return buscarCodigoPelaLetra(noAtual.filhoDireita, letraAlvo, caminhoAteAqui + "-");
        }

        // Calcula a altura da árvore.
        public int calcularAltura() {
            return calcularAlturaRecursivamente(raiz);
        }

        private int calcularAlturaRecursivamente(NoDaArvore no) {
            if (no == null) {
                return 0;
            }
            return 1 + Math.max(calcularAlturaRecursivamente(no.filhoEsquerda), calcularAlturaRecursivamente(no.filhoDireita));
        }

        // Prepara o "pincel" e manda desenhar a árvore inteira.
        public void desenharArvoreCompleta(Canvas telaDeDesenho) {
            GraphicsContext pincel = telaDeDesenho.getGraphicsContext2D();
            pincel.clearRect(0, 0, telaDeDesenho.getWidth(), telaDeDesenho.getHeight());
            pincel.setStroke(Color.BLACK);
            pincel.setLineWidth(1.5);
            pincel.setFont(new Font("Arial", 14));
            desenharNo(pincel, raiz, telaDeDesenho.getWidth() / 2, 40, telaDeDesenho.getWidth() / 4);
        }

        // A função recursiva que desenha cada bolinha e as linhas.
        private void desenharNo(GraphicsContext pincel, NoDaArvore no, double x, double y, double distanciaHorizontalFilho) {
            if (no == null) {
                return;
            }
            pincel.strokeOval(x - 15, y - 15, 30, 30);
            pincel.fillText(String.valueOf(no.letra), x - 5, y + 5);
            if (no.filhoEsquerda != null) {
                double filhoX = x - distanciaHorizontalFilho;
                double filhoY = y + 80;
                pincel.strokeLine(x - 10, y + 10, filhoX + 10, filhoY - 10);
                desenharNo(pincel, no.filhoEsquerda, filhoX, filhoY, distanciaHorizontalFilho / 2);
            }
            if (no.filhoDireita != null) {
                double filhoX = x + distanciaHorizontalFilho;
                double filhoY = y + 80;
                pincel.strokeLine(x + 10, y + 10, filhoX - 10, filhoY - 10);
                desenharNo(pincel, no.filhoDireita, filhoX, filhoY, distanciaHorizontalFilho / 2);
            }
        }
    }

    // Esse é o método 'start', que cria a janela gráfica.
    @Override
    public void start(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Visualizador da Árvore Morse");

        // IMPORTANTE: Agora ele não cria uma árvore nova, ele usa a árvore estática
        // que já foi criada (e talvez modificada) lá no menu do 'main'.
        int alturaDaArvore = arvore.calcularAltura();
        int alturaDaTela = 50 + alturaDaArvore * 80;
        int larguraDaTela = (int) Math.pow(2, alturaDaArvore) * 50;
        Canvas telaDeDesenho = new Canvas(Math.max(800, larguraDaTela), alturaDaTela);
        arvore.desenharArvoreCompleta(telaDeDesenho);
        Group grupoRaiz = new Group();
        grupoRaiz.getChildren().add(telaDeDesenho);
        Scene cena = new Scene(grupoRaiz);
        palcoPrincipal.setScene(cena);
        palcoPrincipal.show();
    }

    // O 'main' é o ponto de partida de tudo.
    public static void main(String[] args) {
        // Crio a árvore estática assim que o programa começa.
        arvore = new ArvoreMorse();
        popularArvoreComPadroes(); // Já coloco o alfabeto nela.

        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1: lidarComInsercao(scanner); break;
                    case 2: lidarComCodificacao(scanner); break;
                    case 3: lidarComDecodificacao(scanner); break;
                    case 4: lidarComExibicaoDaArvore(args); break; // AQUI CHAMA A JANELA
                    case 0: System.out.println("Encerrando o programa..."); break;
                    default: System.out.println("Opção inválida. Tente novamente."); break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, insira um número válido para a opção.");
            }
            System.out.println(); // Linha em branco pra separar os menus
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("--- Menu Código Morse com Árvore Binária ---");
        System.out.println("1. Inserir um novo caractere");
        System.out.println("2. Codificar texto para Morse");
        System.out.println("3. Decodificar Morse para texto");
        System.out.println("4. Exibir a Árvore Gráfica");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void lidarComInsercao(Scanner scanner) {
        System.out.print("Digite o caractere a ser inserido: ");
        String entrada = scanner.nextLine();
        if (entrada.length() != 1) {
            System.out.println("Erro: Insira apenas um único caractere.");
            return;
        }
        char caractere = entrada.toUpperCase().charAt(0);

        System.out.print("Digite o código Morse correspondente (use '.' e '-'): ");
        String codigoMorse = scanner.nextLine();

        if (!codigoMorse.matches("[.-]+")) {
            System.out.println("Erro: O código Morse deve conter apenas '.' e '-'.");
            return;
        }
        arvore.inserirLetra(caractere, codigoMorse);
        System.out.println("Caractere '" + caractere + "' inserido com sucesso!");
    }

    private static void lidarComCodificacao(Scanner scanner) {
        if (arvore.estaVazia()) {
            System.out.println("Erro: A árvore está vazia. Não é possível codificar.");
            return;
        }
        System.out.print("Digite o texto para codificar (apenas letras e espaços): ");
        String texto = scanner.nextLine();
        if (!texto.matches("[a-zA-Z ]+")) {
            System.out.println("Erro: O texto deve conter apenas letras e espaços.");
            return;
        }
        String textoCodificado = arvore.codificarParaMorse(texto);
        System.out.println("Texto codificado: " + textoCodificado);
    }

    private static void lidarComDecodificacao(Scanner scanner) {
        if (arvore.estaVazia()) {
            System.out.println("Erro: A árvore está vazia. Não é possível decodificar.");
            return;
        }
        System.out.print("Digite o Morse para decodificar (use ' ' entre letras e '/' entre palavras): ");
        String morse = scanner.nextLine();
        if (!morse.matches("[.\\- /]+")) {
            System.out.println("Erro: Código Morse inválido. Use apenas '.', '-', ' ' e '/'.");
            return;
        }
        morse = morse.replace("/", " space ");
        String textoDecodificado = arvore.decodificarMorse(morse).replace("space", " ");
        System.out.println("Texto decodificado: " + textoDecodificado);
    }

    private static void lidarComExibicaoDaArvore(String[] args) {
        if (arvore.estaVazia()) {
            System.out.println("A árvore está vazia, não há nada para exibir.");
            return;
        }
        System.out.println("Iniciando a interface gráfica da árvore...");
        // Esse comando inicia a aplicação JavaFX, que vai chamar o método 'start' lá em cima.
        launch(args);
    }

    private static void popularArvoreComPadroes() {
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
    }
}
