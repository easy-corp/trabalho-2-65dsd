package visao;


import controle.Malha;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import modelo.ui.UiCarro;

public class TelaSimulador extends VBox{

    private TelaMalhaPrincipal telaMalha;
    private Malha malha;

    private Label contadorCarros;
    private TextField intputQtdCarros;

    public TelaSimulador(TelaMalhaPrincipal telaMalha){
        this.telaMalha = telaMalha;
        this.malha = Malha.getInstance();
        this.createComponents();
        this.loadHandlers();
    }

    private void loadHandlers(){
        this.malha.getContadorCarros().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                contadorCarros.setText(newValue.toString());
            }
        });
    }

    private void createComponents(){
        this.getChildren().add(this.getTelaControles());
        this.getChildren().add(this.telaMalha.createContent());
    }

    private Pane getTelaControles(){
        VBox telaControles = new VBox();
        telaControles.setAlignment(Pos.CENTER);
        telaControles.setPrefHeight(100);



        Font fonteGrande = new Font("sans-serif", 18);
        Font fontePequena = new Font("sans-serif", 14);

        contadorCarros = new Label();
        contadorCarros.setFont(fonteGrande);

        Label labelContador = new Label("Carros Ativos: ");
        labelContador.setFont(fontePequena);


        Label labelQtdCarrosAtivos = new Label("Quantidade de Carros: ");
        labelContador.setFont(fontePequena);



        HBox botoes = new HBox();
        botoes.setAlignment(Pos.CENTER);
        botoes.setSpacing(10);
        
        Button buttonPlay = new Button("Iniciar");
        buttonPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int qtdCarros = Integer.parseInt(intputQtdCarros.getText());
                malha.setQtdCarros(qtdCarros);

                for(int i=0; i< qtdCarros; i++){
                    malha.spawnarCarro();
                }

                for(UiCarro uic : malha.getCarros()){
                    uic.getCarro().start();
                }

                buttonPlay.setDisable(true);
                intputQtdCarros.setDisable(true);
                
            }
            
        });

        intputQtdCarros = new TextField();
        intputQtdCarros.setMaxWidth(60);
        intputQtdCarros.setText("10");

        intputQtdCarros.setOnKeyTyped(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {


                boolean isNumeric = false;

                try {
                    Double.parseDouble(intputQtdCarros.getText().trim());
                    isNumeric = true;
                } catch (Exception e) {
                    isNumeric = false;
                }

                buttonPlay.setDisable(!isNumeric);
            }
            
        });


        Button buttonEncerrar = new Button("Encerrar");
        buttonEncerrar.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                malha.setDestroy(true);
                for(UiCarro c : malha.getCarros()){
                    c.getCarro().interrupt();
                    c.getCarro().destruirCarro();
                }

                for(UiCarro c : malha.getCarrosAtivos()){
                    c.getCarro().interrupt();
                    c.getCarro().destruirCarro();
                }

                intputQtdCarros.setDisable(false);
                buttonPlay.setDisable(false);
            }
            
        });


        botoes.getChildren().add(buttonPlay);
        botoes.getChildren().add(buttonEncerrar);
        VBox.setMargin(botoes, new Insets(10));

   
        contadorCarros.setFont(fonteGrande);

        telaControles.getChildren().add(labelContador);
        telaControles.getChildren().add(contadorCarros);
        telaControles.getChildren().add(labelQtdCarrosAtivos);
        telaControles.getChildren().add(intputQtdCarros);
        telaControles.getChildren().add(botoes);


        return telaControles;
    }



    
}
