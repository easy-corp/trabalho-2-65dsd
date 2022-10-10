package modelo.ui;

import javafx.scene.shape.Rectangle;

public class UiCasa extends Rectangle {
    
    private int[] posicao;
    private int telaSize;

    public UiCasa(int x, int y, int telaSize) {
        this.posicao = new int[] {x, y};
        this.telaSize = telaSize;

        //Define tamanho com base no atributo do tamanho
        setWidth(this.telaSize);
        setHeight(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.posicao[0] * this.telaSize) , (this.posicao[1] * telaSize));
    }

}
