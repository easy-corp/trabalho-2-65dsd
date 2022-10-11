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
        this.leitor = new LerArquivo("recursos/malha-exemplo-1.txt");

        //Teste da leitura do arquivo
        // for (int i = 0; i < leitor.getMalha().length; i++) {
        //     for (int n = 0; n < leitor.getMalha()[i].length; n++) {
        //         System.out.print(leitor.getMalha()[i][n] + " - ");
        //     }
            
        //     System.out.println();
        // }

        this.telaMalha = new telaMalha(this.leitor.getWidth(), this.leitor.getHeight());
        geraMalha(leitor.getMalha());
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
