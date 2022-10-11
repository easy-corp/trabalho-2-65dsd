package modelo.ui;

import java.io.File;
import java.io.FileInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Casa;

public class UiSimbolo extends ImageView {
    
    private int[] posicao;
    private int telaSize;
    private Casa casa;

    public UiSimbolo(int x, int y, int telaSize, Casa casa) {
        this.posicao = new int[] {x, y};
        this.telaSize = telaSize;
        this.casa = casa;
		
        //Define tamanho com base no atributo do tamanho
		setFitHeight(this.telaSize);
		setFitWidth(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.posicao[1] * this.telaSize) , (this.posicao[0] * telaSize));

        //Define e seta a imagem
        Image img = new Image(new File("recursos/setas/" + this.casa.getSimbolo() + ".png").toURI().toString());
        setImage(img);
    }

}
