package modelo.ui;

import java.io.File;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Carro;
import modelo.Casa.TipoCasa;

public class UiCarro extends ImageView {

    private Carro carro;
    private int telaSize;

    public UiCarro(Carro carro, int telaSize) {
        this.carro = carro;
        this.telaSize = telaSize;
        carro.setUi(this);

        //Define tamanho com base no atributo do tamanho
		setFitHeight(this.telaSize);
		setFitWidth(this.telaSize);

        //Altera a posicao para formar o tabuleiro
        relocate((this.carro.getPosicao().getY() * this.telaSize) , (this.carro.getPosicao().getX() * this.telaSize));

        //Define a imagem
        Image img = new Image(new File("recursos/carros/" + this.carro.getDirecao() + ".png").toURI().toString());
        setImage(img);
    }

    //Move o carro
    public void mover(Point2D where, TipoCasa direcao) {
        this.carro.mover(where, direcao);
        atualizarPosicao();
    }

    //Atualiza imagem e posicao do carro
    private void atualizarPosicao() {
        //Altera a posicao para formar o tabuleiro
        relocate((this.carro.getPosicao().getY() * this.telaSize) , (this.carro.getPosicao().getX() * this.telaSize));

        //Define a imagem
        Image img = new Image(new File("recursos/carros/" + this.carro.getDirecao() + ".png").toURI().toString());
        setImage(img);
    }

}