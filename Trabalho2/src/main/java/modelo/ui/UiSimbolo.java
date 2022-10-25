package modelo.ui;

import java.io.File;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Casa;

public class UiSimbolo extends ImageView {
    
    private Point2D posicao;
    private int telaSize;
    private Casa casa;

    public UiSimbolo(Point2D posicao, int telaSize, Casa casa) {
        this.posicao = posicao;
        this.telaSize = telaSize;
        this.casa = casa;
		
        //Define tamanho com base no atributo do tamanho
		setFitHeight(this.telaSize);
		setFitWidth(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.posicao.getY() * this.telaSize) , (this.posicao.getX() * telaSize));

        //Define e seta a imagem
        Image img = new Image(new File("recursos/setas/" + this.casa.getTipo().name() + ".png").toURI().toString());
        setImage(img);
    }

}
