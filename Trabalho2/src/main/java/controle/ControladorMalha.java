package controle;

import java.io.IOException;

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

public class ControladorMalha {

    private TelaMalha telaMalha;
    private Malha malha;

    public ControladorMalha() throws IOException {
        this.malha = Malha.getInstance();

        lerArquivo();
        iniciarTela();

        // spawna em entradas fixas por enquanto
        spawnarCarro(0);
        spawnarCarro(1);
        spawnarCarro(2);
        spawnarCarro(3);

    }

    //Abre a tela
    public void iniciarTela() {
        Scene cena = new Scene(this.telaMalha.createContent());

        Stage stageJogo = new Stage();

        stageJogo.setTitle("Malha de trânsito");
        stageJogo.setScene(cena);

        stageJogo.show();
    }

    //Le o arquivo
    public void lerArquivo() throws IOException {
        new LeitorArquivo("recursos/malha-exemplo-3.txt");

        this.telaMalha = new TelaMalha(this.malha.getWidth(), this.malha.getHeight());
        geraMalha(this.malha.getMalha());
    }

    //Gera a malha na tela
    public void geraMalha(Casa[][] malha) {
        //Percorre linhas
        for (int x = 0; x < this.malha.getWidth(); x++) {
            //Percorre colunas
            for (int y = 0; y < this.malha.getHeight(); y++) {
                //Cria casinha com base na posicao da malha vinda do arquivo
                UiCasa casa = new UiCasa(new Point2D(x, y), this.telaMalha.getSize(), malha[x][y]);

                //Cria Símbolo com base na posição da malha vinda do arquivo
                UiSimbolo simbolo = new UiSimbolo(new Point2D(x, y), this.telaMalha.getSize(), malha[x][y]);

                //Adiciona na tela
                this.telaMalha.getGrupoMalha().getChildren().add(casa);
                this.telaMalha.getGrupoMalha().getChildren().add(simbolo);
            }
        }
    }

    //Insere um carro em uma das entradas
    public void spawnarCarro(int posSpawn) {
        //Recupera uma posicao aleatoria dentre as entradas
        //int posSpawn = new Random().nextInt(this.malha.getPosEntradas().size());
        Point2D posAleatoria = this.malha.getPosEntradas().get(posSpawn);

        //Recupera a posicao a qual o carro esta virado
        TipoCasa direcao = this.malha.getCasa(posAleatoria).getTipo();

        //Cria o carro
        Carro carro = new Carro(posAleatoria, direcao);
        UiCarro uiCarro = new UiCarro(carro, this.telaMalha.getSize());

        //Adiciona o carro na tela
        this.telaMalha.getGrupoCarros().getChildren().add(uiCarro);

        //Adiciona o carro na lista para manipular
        this.malha.addCarro(uiCarro);

        // inicia a thread do carro
        carro.start();
    }

}
