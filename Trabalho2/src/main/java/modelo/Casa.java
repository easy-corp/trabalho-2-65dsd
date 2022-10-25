package modelo;

import java.util.concurrent.Semaphore;

import javafx.scene.paint.Color;

public class Casa {

    // O objeto casa contem a sua cor e o simbolo (direcao para o qual ele aponta)
    private Color cor;
    private TipoCasa tipo;
    private Semaphore mutex;

    public Casa(Color cor, TipoCasa tipo) {
        this.cor = cor;
        this.tipo = tipo;
        this.mutex = new Semaphore(1);
    }

    public Color getCor() {
        return this.cor;
    }

    public TipoCasa getTipo() {
        return this.tipo;
    }

    public void acquire() throws InterruptedException{
        this.mutex.acquire();
        System.out.println("Acquire realizado: " + this.mutex.availablePermits());
    }

    public void release(){
        this.mutex.release();
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

}
