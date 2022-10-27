package modelo;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import controle.Malha;
import javafx.scene.paint.Color;
import modelo.ui.UiCasa;

public class Casa {

    // O objeto casa contem a sua cor e o simbolo (direcao para o qual ele aponta)
    private Color cor;
    private TipoCasa tipo;
    private Semaphore mutex;
    private Semaphore mutexCruzamento;
    private UiCasa ui;
    private Malha malha;

    public Casa(Color cor, TipoCasa tipo) {
        this.cor = cor;
        this.tipo = tipo;
        this.mutex = new Semaphore(1);
        this.malha = Malha.getInstance();
    }

    public Color getCor() {
        return this.cor;
    }

    public TipoCasa getTipo() {
        return this.tipo;
    }

    public void acquireCasa() throws InterruptedException {
        this.mutex.acquire();

        //Para testes
        if (malha.getDebug()) {
            this.ui.setFill(Color.BLACK);
        }
    }

    public UiCasa getUi() {
        return ui;
    }

    public void setUi(UiCasa ui) {
        this.ui = ui;
    }

    public void releaseCasa() {
        this.mutex.release();
        
        //Para teste
        if (malha.getDebug()) {
            this.ui.setFill(this.cor);
        }
    }

    public void acquireCruzamento() throws InterruptedException{
        if(this.mutexCruzamento instanceof Semaphore){
            this.mutexCruzamento.acquire();
        }
    }

    public void releaseCruzamento() throws InterruptedException{
        if(this.mutexCruzamento instanceof Semaphore){
            this.mutexCruzamento.release();
        }
    }

    public Semaphore getMutexCruzamento() {
        return mutexCruzamento;
    }

    public void setMutexCruzamento(Semaphore mutexCruzamento) {
        this.mutexCruzamento = mutexCruzamento;
    }

    public enum TipoCasa {

        TIPO_EMPTY,
        TIPO_UP,
        TIPO_DOWN,
        TIPO_LEFT,
        TIPO_RIGHT,
        TIPO_CRUZAMENTO_UP,
        TIPO_CRUZAMENTO_DOWN,
        TIPO_CRUZAMENTO_RIGHT,
        TIPO_CRUZAMENTO_LEFT,
        TIPO_CRUZAMENTO_UP_RIGHT,
        TIPO_CRUZAMENTO_UP_LEFT,
        TIPO_CRUZAMENTO_DOWN_RIGHT,
        TIPO_CRUZAMENTO_DOWN_LEFT

    }

    @Override
    public String toString() {
        return this.getTipo().name() + " " + this.getUi().getPosicao();
    }

}
