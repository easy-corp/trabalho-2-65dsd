package modelo.ui;

import java.io.File;

import controle.Malha;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Carro;

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
        relocate((this.carro.getPosicao()[1] * this.telaSize) , (this.carro.getPosicao()[0] * this.telaSize));

        //Define a imagem
        Image img = new Image(new File("recursos/carros/" + this.carro.getDirecao() + ".png").toURI().toString());
        setImage(img);
    }

    //Move o carro
    public void mover(int x, int y, String direcao) {
        this.carro.mover(x, y, direcao);

        atualizarPosicao();
    }

    //Atualiza imagem e posicao do carro
    private void atualizarPosicao() {
        //Altera a posicao para formar o tabuleiro
        relocate((this.carro.getPosicao()[1] * this.telaSize) , (this.carro.getPosicao()[0] * this.telaSize));

        //Define a imagem
        Image img = new Image(new File("recursos/carros/" + this.carro.getDirecao() + ".png").toURI().toString());
        setImage(img);
    }

}