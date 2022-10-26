package visao;

import controle.Malha;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TelaSimulador extends VBox{

    private TelaMalha telaMalha;
    private Malha malha;

    private Label contadorCarros;

    public TelaSimulador(TelaMalha telaMalha){
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
        HBox telaControles = new HBox();
        telaControles.setAlignment(Pos.CENTER);
        telaControles.setPrefHeight(100);

        contadorCarros = new Label();

        Font fonteGrande = new Font("sans-serif", 18);
        contadorCarros.setFont(fonteGrande);

        Font fontePequena = new Font("sans-serif", 14);
        contadorCarros.setFont(fonteGrande);

        Label labelContador = new Label("Carros Ativos: ");
        labelContador.setFont(fontePequena);


   
        contadorCarros.setFont(fonteGrande);

        telaControles.getChildren().add(labelContador);
        telaControles.getChildren().add(contadorCarros);

        return telaControles;
    }
    
}
