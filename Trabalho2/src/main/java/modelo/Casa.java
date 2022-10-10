package modelo;

import javafx.scene.paint.Color;

public class Casa {

    //O objeto casa contem a sua cor e o simbolo (direcao para o qual ele aponta)
    private Color cor;
    private String simbolo;

    public Casa(Color cor, String simbolo) {
        this.cor = cor;
        this.simbolo = simbolo;
    }

    public Color getCor() {
        return this.cor;
    }

    public String getSimbolo() {
        return this.simbolo;
    }

}
