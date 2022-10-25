package controle;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import modelo.Casa;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;

public class Malha {

    // Singleton guardando os dados importantes para o projeto

    private static Malha instance;
    private int width; // Largura da malha
    private int height; // Altura da malha
    private Casa[][] malha; // A malha
    private List<Point2D> posEntradas = new ArrayList<>(); // Lista das posições de entrada
    private List<Point2D> posSaidas = new ArrayList<>(); // Lista das posições de saida
    private List<UiCarro> carros = new ArrayList<>(); // Lista dos carros inseridos

    private Malha() {
    }

    public synchronized static Malha getInstance() {
        if (instance == null) {
            instance = new Malha();
        }

        return instance;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void setMalha(Casa[][] malha) {
        this.malha = malha;
    }

    public Casa[][] getMalha() {
        return this.malha;
    }

    public Casa getCasa(Point2D where){
        return this.malha[(int)where.getX()][(int)where.getY()];
    }

    public void addPosEntrada(Point2D entrada) {
        this.posEntradas.add(entrada);
    }

    public List<Point2D> getPosEntradas() {
        return this.posEntradas;
    }

    public void addPosSaida(Point2D saida) {
        this.posSaidas.add(saida);
    }

    public List<Point2D> getPosSaidas() {
        return this.posSaidas;
    }

    public void addCarro(UiCarro carro) {
        this.carros.add(carro);
    }

    public List<UiCarro> getCarros() {
        return this.carros;
    }

    public Casa getProximaCasa(Point2D posicaoAtual, TipoCasa direcao){
        return this.getCasa(this.getProximaPosicao(posicaoAtual, direcao));
    }

    /**
     * Retorna a proxima posição para um carro na malha
     * @param posicaoAtual
     * @param direcao
     * @return Point2D
     */
    public Point2D getProximaPosicao(Point2D posicaoAtual, TipoCasa direcao) {
        switch (direcao) {
            case TIPO_UP:
                return new Point2D(posicaoAtual.getX() - 1, posicaoAtual.getY());
            case TIPO_DOWN:
                return new Point2D(posicaoAtual.getX() + 1, posicaoAtual.getY());
            case TIPO_LEFT:
                return new Point2D(posicaoAtual.getX(), posicaoAtual.getY() - 1);
            default:
                return new Point2D(posicaoAtual.getX(), posicaoAtual.getY() + 1);
        }
    }

}
