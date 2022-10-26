package modelo.ui;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import modelo.Casa;

public class UiCasa extends Rectangle {
    
    private Point2D posicao;
    private int telaSize;
    private Casa casa;

    public UiCasa(Point2D position, int telaSize, Casa casa) {
        this.posicao = position;
        this.telaSize = telaSize;
        this.casa = casa;
        this.casa.setUi(this);

        //Define tamanho com base no atributo do tamanho
        setWidth(this.telaSize);
        setHeight(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.posicao.getX() * this.telaSize) , (this.posicao.getY() * this.telaSize));

        //Preenche a casa com a cor referente
        setFill(this.casa.getCor());
        setStyle("-fx-stroke: black; -fx-stroke-width: 1;");
    }

    public Point2D getPosicao() {
        return this.posicao;
    }

}
