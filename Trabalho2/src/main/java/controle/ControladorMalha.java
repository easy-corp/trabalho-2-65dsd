package controle;

import java.io.IOException;
import java.util.Random;

import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Carro;
import modelo.Casa;
import modelo.ui.UiCarro;
import modelo.ui.UiCasa;
import modelo.ui.UiSimbolo;
import visao.telaMalha;

public class ControladorMalha {

    private telaMalha telaMalha;
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

        //TESTE//
        //movimentarCarro(7, 8);
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
        LeitorArquivo leitor = new LeitorArquivo("recursos/malha-exemplo-3.txt");

        this.telaMalha = new telaMalha(this.malha.getWidth(), this.malha.getHeight());
        geraMalha(this.malha.getMalha());
    }

    //Gera a malha na tela
    public void geraMalha(Casa[][] malha) {
        //Percorre linhas
        for (int x = 0; x < this.malha.getWidth(); x++) {
            //Percorre colunas
            for (int y = 0; y < this.malha.getHeight(); y++) {
                //Cria casinha com base na posicao da malha vinda do arquivo
                UiCasa casa = new UiCasa(x, y, this.telaMalha.getSize(), malha[x][y]);

                //Cria Símbolo com base na posição da malha vinda do arquivo
                UiSimbolo simbolo = new UiSimbolo(x, y, this.telaMalha.getSize(), malha[x][y]);

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
        int[] posAleatoria = this.malha.getPosEntradas().get(posSpawn);

        //Recupera a posicao a qual o carro esta virado
        String posicao = this.malha.getMalha()[posAleatoria[0]][posAleatoria[1]].getSimbolo();

        //Cria o carro
        Carro carro = new Carro(posAleatoria[0], posAleatoria[1], posicao);
        UiCarro uiCarro = new UiCarro(carro, this.telaMalha.getSize());

        //Adiciona o carro na tela
        this.telaMalha.getGrupoCarros().getChildren().add(uiCarro);

        //Adiciona o carro na lista para manipular
        this.malha.addCarro(uiCarro);

        // inicia a thread do carro
        carro.start();
    }

    //Movimentacao do carro
    public void movimentarCarro(int x, int y) {
        UiCarro carro = this.malha.getCarros().get(0);

        carro.mover(x, y, this.malha.getMalha()[x][y].getSimbolo());
    }

}
