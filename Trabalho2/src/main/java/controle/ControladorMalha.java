package controle;

import java.io.IOException;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Casa;
import modelo.ui.UiCarro;
import modelo.ui.UiCasa;
import modelo.ui.UiSimbolo;
import visao.TelaMalhaPrincipal;
import visao.TelaSimulador;

public class ControladorMalha {

    private TelaMalhaPrincipal telaMalha;
    private TelaSimulador telaSimulador;
    private Malha malha;
    private Stage stage;

    public ControladorMalha(Stage stage) throws IOException {
        this.malha = Malha.getInstance();
        this.stage = stage;

        lerArquivo();
        
        this.telaSimulador = new TelaSimulador(this.telaMalha);

        iniciarTela();

        setAcaoBtn();
        
    }

    // Abre a tela
    private void iniciarTela() {
        Scene cena = new Scene(this.telaSimulador);

        stage.setTitle("Malha de trânsito");
        stage.setScene(cena);

        stage.show();
    }

    // Le o arquivo
    private void lerArquivo() throws IOException {
        new LeitorArquivo("recursos/malha-exemplo-3.txt");

        this.telaMalha = new TelaMalhaPrincipal(this.malha.getWidth(), this.malha.getHeight());
        this.malha.setTelaMalha(telaMalha);
        geraMalha(this.malha.getMalha());
    }

    // Gera a malha na tela
    private void geraMalha(Casa[][] malha) {
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

    // Define acao dos botoes
    public void setAcaoBtn() {

        // Acao botao PLAY
        this.telaSimulador.setAcaoBtnPlay(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                int qtdCarros = Integer.parseInt(telaSimulador.getTextInpQtdCarros());
                malha.setQtdCarros(qtdCarros);

                for(int i=0; i< qtdCarros; i++){
                    malha.spawnarCarro();
                }

                for(UiCarro uic : malha.getCarros()){
                    uic.getCarro().start();
                }

                telaSimulador.setDisableBtnPlay(true);
                telaSimulador.setDisableInpQtdCarros(true);
                malha.setDestroy(false);
                telaSimulador.setDisableBtnEncerrar(false);
                telaSimulador.setDisableBtnDebug(false);
            }
            
        });

        // Acao botao ENCERRAR
        this.telaSimulador.setAcaoBtnEncerrar(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                malha.setDestroy(true);

                for(UiCarro c : malha.getCarros()){
                    c.getCarro().setDestroy(true);
                    c.getCarro().destruirCarro();
                }
                for(UiCarro c : malha.getCarrosAtivos()){
                    c.getCarro().setDestroy(true);
                    c.getCarro().destruirCarro();
                }

                malha.clearCarros();

                telaSimulador.removeChildren(1);
                TelaMalhaPrincipal novaTelaMalha = new TelaMalhaPrincipal(malha.getWidth(), malha.getHeight());
                malha.setTelaMalha(novaTelaMalha);
                geraMalha(malha.getMalha());
                telaSimulador.addChildren(novaTelaMalha.createContent());

                telaSimulador.setDisableInpQtdCarros(false);
                telaSimulador.setDisableBtnPlay(false);
                telaSimulador.setDisableBtnEncerrar(true);
                telaSimulador.setDisableBtnDebug(true);
            }
            
        });

        // Acao botao DEBUG
        this.telaSimulador.setAcaoBtnDebug(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                if (malha.getDebug()) {
                    malha.setDebug(false);
                } else {
                    malha.setDebug(true);
                }
            }
            
        });

        // Acao botao INPUT QTD CARROS
        this.telaSimulador.setAcaoInpQtdCarros(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                boolean isNumeric = false;

                try {
                    Double.parseDouble(telaSimulador.getTextInpQtdCarros().trim());
                    isNumeric = true;
                } catch (Exception e) {
                    isNumeric = false;
                }

                telaSimulador.setDisableBtnPlay(!isNumeric);
            }
            
        });
    }
}