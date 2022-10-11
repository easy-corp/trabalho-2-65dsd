package controle;

import java.io.IOException;

import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Casa;
import modelo.ui.UiCasa;
import modelo.ui.UiSimbolo;
import visao.telaMalha;

public class ControladorMalha {

    private telaMalha telaMalha;
    private LerArquivo leitor;
    
    public ControladorMalha() throws IOException {
        lerArquivo();
        iniciarTela();
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
        this.leitor = new LerArquivo("recursos/malha-exemplo-3.txt");

        this.telaMalha = new telaMalha(this.leitor.getWidth(), this.leitor.getHeight());
        geraMalha(leitor.getMalha());

        //Imprime entradas
        System.out.println("---- Entradas ----");
        for (int[] i : this.leitor.getEntradas()) {
            System.out.println(i[0] + " - " + i[1]);
        }

        //Imprime saidas
        System.out.println("---- Saidas ----");
        for (int[] i : this.leitor.getSaidas()) {
            System.out.println(i[0] + " - " + i[1]);
        }
    }

    //Gera a malha na tela
    public void geraMalha(Casa[][] malha) {
        //Percorre linhas
        for (int x = 0; x < this.leitor.getWidth(); x++) {
            //Percorre colunas
            for (int y = 0; y < this.leitor.getHeight(); y++) {
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

}
