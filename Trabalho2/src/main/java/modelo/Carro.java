package modelo;

import controle.Malha;
import javafx.geometry.Point2D;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;

public class Carro extends Thread {

    private Point2D posicao;       //Posicionamento do carro, X e Y, respectivamente
    private TipoCasa direcao;      //Direcao para onde o carro esta indo
    private UiCarro ui;          //UI do carro

    public Carro(Point2D spawnPoint, TipoCasa direcao) {
        mover(spawnPoint, direcao);
    }

    public Point2D getPosicao() {
        return this.posicao;
    }

    public void setDirecao(TipoCasa direcao) {
        //Se o carro estiver em um cruzamento, nao vamos alterar a sua posicao
        //Cruzamentos possuem CRUZAMENTO em sua descricao na Malha
        if (!direcao.name().contains("CRUZAMENTO")) {
            this.direcao = direcao;
        }
    }

    public TipoCasa getDirecao() {
        return this.direcao;
    }

    //Movimento do carro
    public void mover(Point2D posicao, TipoCasa direcao) {
        this.posicao = posicao;
        this.setDirecao(direcao);
    }

    public UiCarro getUi() {
        return ui;
    }

    public void setUi(UiCarro ui) {
        this.ui = ui;
    }

    @Override
    public void run() {
        try {
            // por enquanto vai ate o cruzamento
            // nao trata carro na mesma casa
            Malha malha = Malha.getInstance();
            Casa casaAtual = malha.getCasa(this.getPosicao());
            Point2D proximaPosicao = malha.getProximaPosicao(getPosicao(), getDirecao());
            Casa proximaCasa = malha.getCasa(proximaPosicao);

            while (/*!casa.getTipo().name().contains("CRUZAMENTO")*/ true) {
                proximaCasa.acquire();
                casaAtual.release();
                ui.mover(proximaPosicao, this.getDirecao());
                casaAtual = proximaCasa;
                proximaPosicao = malha.getProximaPosicao(proximaPosicao, this.getDirecao());
                proximaCasa = malha.getCasa(proximaPosicao);
                sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Deu ruim");
        }
    }

}