import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
// Outros imports que a classe já tenha...

public class TreeVisualizer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // --- ESTA É A LINHA QUE VOCÊ PRECISA ADICIONAR/MODIFICAR ---
        // Pega a raiz da árvore estática que está na classe Main.
        Node root = Main.morseTree.getRoot();

        // O resto do código provavelmente já existe no seu arquivo.
        // Ele usa a variável 'root' para começar a desenhar.
        Pane pane = new Pane();
        // O nome do método e os parâmetros podem ser diferentes no seu arquivo.
        // Adapte se necessário.
        displayTree(pane, root, 400, 50, 200); 

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Visualizador da Árvore Morse");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // O resto do seu código do TreeVisualizer.java (método displayTree, etc.)
    // deve permanecer o mesmo.
    private void displayTree(Pane pane, Node root, double x, double y, double hGap) {
        // ... Lógica de desenho que você recebeu ...
    }
}