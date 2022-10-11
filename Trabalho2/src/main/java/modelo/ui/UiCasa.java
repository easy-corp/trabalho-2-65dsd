package modelo.ui;

import javafx.scene.shape.Rectangle;
import modelo.Casa;

public class UiCasa extends Rectangle {
    
    private int[] posicao;
    private int telaSize;
    private Casa casa;

    public UiCasa(int x, int y, int telaSize, Casa casa) {
        this.posicao = new int[] {x, y};
        this.telaSize = telaSize;
        this.casa = casa;

        //Define tamanho com base no atributo do tamanho
        setWidth(this.telaSize);
        setHeight(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.posicao[1] * this.telaSize) , (this.posicao[0] * telaSize));

        //Preenche a casa com a cor referente
        setFill(this.casa.getCor());
        setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
    }

}
