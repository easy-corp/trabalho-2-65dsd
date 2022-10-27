package controle;

import java.io.IOException;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Casa;
import modelo.ui.UiCasa;
import modelo.ui.UiSimbolo;
import visao.TelaMalhaPrincipal;
import visao.TelaSimulador;

public class ControladorMalha {

    private TelaMalhaPrincipal telaMalha;
    private Malha malha;
    private Stage stage;
    public ControladorMalha(Stage stage) throws IOException {
        this.malha = Malha.getInstance();
        this.stage = stage;

        lerArquivo();
        iniciarTela();


        for (Point2D p : this.malha.getPosSaidas()) {
            System.out.println(p);
        }
    }

    // Abre a tela
    public void iniciarTela() {
        Scene cena = new Scene(new TelaSimulador(this.telaMalha));

        stage.setTitle("Malha de trânsito");
        stage.setScene(cena);

        stage.show();
    }

    // Le o arquivo
    public void lerArquivo() throws IOException {
        new LeitorArquivo("recursos/malha-exemplo-3.txt");

        this.telaMalha = new TelaMalhaPrincipal(this.malha.getWidth(), this.malha.getHeight());
        this.malha.setTelaMalha(telaMalha);
        geraMalha(this.malha.getMalha());
    }

    // Gera a malha na tela
    public void geraMalha(Casa[][] malha) {
        // Percorre linhas
        for (int x = 0; x < this.malha.getHeight(); x++) {
            // Percorre colunas
            for (int y = 0; y < this.malha.getWidth(); y++) {
                // Cria casinha com base na posicao da malha vinda do arquivo
                UiCasa casa = new UiCasa(new Point2D(x, y), this.telaMalha.getSize(), malha[y][x]);

                // Cria Símbolo com base na posição da malha vinda do arquivo
                UiSimbolo simbolo = new UiSimbolo(new Point2D(x, y), this.telaMalha.getSize(), malha[y][x]);

                // Adiciona na tela
                this.telaMalha.getGrupoMalha().getChildren().add(casa);
                this.telaMalha.getGrupoMalha().getChildren().add(simbolo);
            }
        }
    }

    // Insere um carro em uma das entradas


}