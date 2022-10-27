package controle;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
                UiCasa casa = new UiCasa(new Point2D(x, y), TelaMalhaPrincipal.size, malha[y][x]);

                // Cria Símbolo com base na posição da malha vinda do arquivo
                UiSimbolo simbolo = new UiSimbolo(new Point2D(x, y), TelaMalhaPrincipal.size, malha[y][x]);

                // Adiciona na tela
                this.telaMalha.getGrupoMalha().getChildren().add(casa);
                this.telaMalha.getGrupoMalha().getChildren().add(simbolo);
            }
        }
    }

    // Define acao dos botoes
    public void setAcaoBtn() {

        this.telaSimulador.setAcaoRbTipo(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                telaSimulador.setDisableBtnPlay(false);
            }
            
        });

        // Acao botao PLAY
        this.telaSimulador.setAcaoBtnPlay(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                RadioButton rb = (RadioButton) telaSimulador.getRbTipo();
                malha.setTipoThread(rb.getText());

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
                telaSimulador.setDisableRbSemaforo(true);
                telaSimulador.setDisableRbSemaforo(true);
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

                //Retira a malha da tela e insere um nova
                telaSimulador.removeChildren(1);

                TelaMalhaPrincipal novaTelaMalha = new TelaMalhaPrincipal(malha.getWidth(), malha.getHeight());
                telaMalha = novaTelaMalha;
                geraMalha(malha.getMalha());
                malha.setTelaMalha(novaTelaMalha);
                
                telaSimulador.addChildren(novaTelaMalha.createContent());

                malha.setDebug(false);

                telaSimulador.setDisableInpQtdCarros(false);
                telaSimulador.setDisableBtnPlay(true);
                telaSimulador.setDisableBtnEncerrar(true);
                telaSimulador.setDisableBtnDebug(true);
                telaSimulador.setSelectedRbSemaforo(false);
                telaSimulador.setSelectedRbMonitor(false);
                telaSimulador.setDisableRbSemaforo(false);
                telaSimulador.setDisableRbSemaforo(false);
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