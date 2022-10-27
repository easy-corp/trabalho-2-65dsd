package visao;


import controle.Malha;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TelaSimulador extends VBox{

    private TelaMalhaPrincipal telaMalha;
    private Malha malha;

    private Label contadorCarros;
    private TextField intputQtdCarros;
    private Button buttonPlay;
    private Button buttonEncerrar;
    private Button buttonDebugMode;
    private Label lblRadio;
    private ToggleGroup tgGroup;
    private RadioButton RbSemaforo;
    private RadioButton RbMonitor;
    

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

        this.lblRadio = new Label("Selecione o tipo: ");
        this.tgGroup = new ToggleGroup();
        
        this.RbSemaforo = new RadioButton("Semáforo");
        this.RbSemaforo.setToggleGroup(tgGroup);
        this.RbMonitor = new RadioButton("Monitor");
        this.RbMonitor.setToggleGroup(tgGroup);
        
        this.buttonPlay = new Button("Iniciar");

        intputQtdCarros = new TextField();
        intputQtdCarros.setMaxWidth(60);
        intputQtdCarros.setText("10");

        this.buttonEncerrar = new Button("Encerrar");
        buttonEncerrar.setDisable(true);

        this.buttonDebugMode = new Button("Debugar");
        buttonDebugMode.setDisable(true);

        botoes.getChildren().add(lblRadio);
        botoes.getChildren().add(RbSemaforo);
        botoes.getChildren().add(RbMonitor);
        botoes.getChildren().add(buttonPlay);
        botoes.getChildren().add(buttonEncerrar);
        botoes.getChildren().add(buttonDebugMode);
        VBox.setMargin(botoes, new Insets(10));
   
        contadorCarros.setFont(fonteGrande);

        telaControles.getChildren().add(labelContador);
        telaControles.getChildren().add(contadorCarros);
        telaControles.getChildren().add(labelQtdCarrosAtivos);
        telaControles.getChildren().add(intputQtdCarros);
        telaControles.getChildren().add(botoes);

        return telaControles;
    }

    public void setAcaoBtnPlay(EventHandler acao) {
        this.buttonPlay.setOnMouseClicked(acao);
    }

    public void setAcaoBtnEncerrar(EventHandler acao) {
        this.buttonEncerrar.setOnMouseClicked(acao);
    }

    public void setAcaoBtnDebug(EventHandler acao) {
        this.buttonDebugMode.setOnMouseClicked(acao);
    }

    public void setAcaoInpQtdCarros(EventHandler acao) {
        this.intputQtdCarros.setOnKeyTyped(acao);
    }

    public void setTextInpQtdCarros(String num) {
        this.intputQtdCarros.setText(num);
    }

    public String getTextInpQtdCarros() {
        return this.intputQtdCarros.getText();
    }

    public void setDisableBtnPlay(boolean opt) {
        this.buttonPlay.setDisable(opt);
    }

    public void setDisableBtnEncerrar(boolean opt) {
        this.buttonEncerrar.setDisable(opt);
    }

    public void setDisableBtnDebug(boolean opt) {
        this.buttonDebugMode.setDisable(opt);
    }

    public void setDisableInpQtdCarros(boolean opt) {
        this.intputQtdCarros.setDisable(opt);
    }

    public Toggle getRbTipo() {
        return this.tgGroup.getSelectedToggle();
    }

    public void setDisableRbSemaforo(boolean opt) {
        this.RbSemaforo.setDisable(opt);
    }

    public void setDisableRbMonitor(boolean opt) {
        this.RbMonitor.setDisable(opt);
    }

    public void removeChildren(int i) {
        this.getChildren().remove(i);
    }

    public void addChildren(Node no) {
        this.getChildren().add(no);
    }
    
}
