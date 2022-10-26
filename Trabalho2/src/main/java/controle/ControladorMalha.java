package controle;

import java.io.IOException;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Carro;
import modelo.Casa;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;
import modelo.ui.UiCasa;
import modelo.ui.UiSimbolo;
import visao.TelaMalha;
import visao.TelaSimulador;

public class ControladorMalha {

    private TelaMalha telaMalha;
    private Malha malha;
    private Stage stage;
    private Random random = new Random();

    public ControladorMalha(Stage stage) throws IOException {
        this.malha = Malha.getInstance();
        this.stage = stage;

        lerArquivo();
        iniciarTela();

        //Spawna carros em uma das entradas aleatoriamente
        for (int i = 0; i < 20; i ++) {
            spawnarCarro();

            // spawnarCarro(0);
        }

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
        new LeitorArquivo("recursos/malha-exemplo-1.txt");

        this.telaMalha = new TelaMalha(this.malha.getWidth(), this.malha.getHeight());
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
    public void spawnarCarro() {
        // Recupera uma posicao aleatoria dentre as entradas
        int posSpawn = random.nextInt(malha.getPosEntradas().size());

        Point2D posAleatoria = this.malha.getPosEntradas().get(posSpawn);

        // Recupera a posicao a qual o carro esta virado
        TipoCasa direcao = this.malha.getCasa(posAleatoria).getTipo();

        // Cria o carro
        Carro carro = new Carro(posAleatoria, direcao);
        UiCarro uiCarro = new UiCarro(carro, this.telaMalha.getSize());

        // Adiciona o carro na tela
        this.telaMalha.getGrupoCarros().getChildren().add(uiCarro);

        // Adiciona o carro na lista para manipular
        this.malha.addCarro(uiCarro);

        // inicia a thread do carro
        carro.start();
    }

}