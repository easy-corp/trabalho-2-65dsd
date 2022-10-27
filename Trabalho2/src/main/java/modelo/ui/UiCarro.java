package modelo.ui;

import java.io.File;

import javafx.application.Platform;
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

        this.carro.setUi(this);

        // Define tamanho com base no atributo do tamanho
        setFitHeight(this.telaSize);
        setFitWidth(this.telaSize);

        // Altera a posicao para formar o tabuleiro
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                relocate((carro.getPosicao().getY() * telaSize), (carro.getPosicao().getX() * telaSize));

                // Define a imagem
                Image img = new Image(new File("recursos/carros/" + carro.getDirecao() + ".png").toURI().toString());
                setImage(img);
            }

        });

    }

    // Move o carro
    public void mover(Point2D where, TipoCasa direcao) {
        this.carro.mover(where, direcao);
        atualizarPosicao();
    }

    // Atualiza imagem e posicao do carro
    private void atualizarPosicao() {
        // Altera a posicao para formar o tabuleiro
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                relocate((carro.getPosicao().getY() * telaSize), (carro.getPosicao().getX() * telaSize));

                // Define a imagem
                Image img = new Image(
                        new File("recursos/carros/" + carro.getDirecao() + ".png").toURI().toString());
                setImage(img);
            }

        });

    }

    public void finalizarCarro() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                carro = null;
                telaSize = 0;
                setImage(null);
            }
            
        });

    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    

}