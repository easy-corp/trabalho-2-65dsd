package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import controle.Malha;
import javafx.geometry.Point2D;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;

public class Carro extends Thread {

    private Point2D posicao; // Posicionamento do carro, X e Y, respectivamente
    private TipoCasa direcao; // Direcao para onde o carro esta indo
    private UiCarro ui; // UI do carro
    private HashMap<TipoCasa, List<List<TipoCasa>>> listaMovimentos;
    private Random random = new Random();

    public Carro(Point2D spawnPoint, TipoCasa direcao) {
        mover(spawnPoint, direcao);
    }

    public Point2D getPosicao() {
        return this.posicao;
    }

    public void setDirecao(TipoCasa direcao) {
        // Se o carro estiver em um cruzamento, nao vamos alterar a sua posicao
        // Cruzamentos possuem CRUZAMENTO em sua descricao na Malha
        if (!direcao.name().contains("CRUZAMENTO")) {
            this.direcao = direcao;
        }
    }

    public TipoCasa getDirecao() {
        return this.direcao;
    }

    // Movimento do carro
    public void mover(Point2D posicao, TipoCasa direcao) {
        this.posicao = posicao;
        this.setDirecao(Malha.getInstance().getCasa(posicao).getTipo());
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
            Point2D proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
            Casa proximaCasa = malha.getCasa(proximaPosicao);
            casaAtual.acquireCasa();
            Semaphore cruzamentoAtual = null;

            while (true) {
                casaAtual.releaseCasa();
                if (proximaCasa.getTipo().name().contains("CRUZAMENTO")) {
                    proximaCasa.acquireCruzamento();
                    cruzamentoAtual = proximaCasa.getMutexCruzamento();
                    
                    Point2D posAt = proximaPosicao;
                    Casa casaAt = malha.getCasa(posAt);
                    List<TipoCasa> movimentosAFazer = new ArrayList<>();

                    while(casaAt.getTipo().name().contains("CRUZAMENTO") || casaAt.getTipo().name().contains("EMPTY")){
                        posAt = proximaPosicao;
                        casaAt = malha.getCasa(posAt);
                        List<List<TipoCasa>> movimentos = malha.getMovimentosPossiveisCruzamento().get(this.getDirecao());
                        int randomIndex = this.random.nextInt(movimentos.size());
                        movimentosAFazer = movimentos.get(randomIndex);
                        for(TipoCasa dir : movimentosAFazer){
                            posAt = malha.getProximaPosicao(posAt, dir);
                        }

                        casaAt = malha.getCasa(posAt);
                    }
                    casaAtual = casaAt;
                    casaAt.acquireCasa();
                    Point2D proximaPosicaoMovimento = null;
                    
                    for(TipoCasa m : movimentosAFazer){
                        proximaPosicaoMovimento = malha.getProximaPosicao(this.getPosicao(), m);
                        Casa casaMover = malha.getCasa(proximaPosicaoMovimento);
                        ui.mover(proximaPosicaoMovimento, casaMover.getTipo());
                        Thread.sleep(2000);
                    }
                    proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
                    proximaCasa = malha.getCasa(proximaPosicao);
                    
                    // System.out.println(proximaPosicaoMovimento);
                    // proximaPosicao = malha.getProximaPosicao(proximaPosicaoMovimento, this.getDirecao());
                    cruzamentoAtual.release();                   
                } else {
                    proximaCasa.acquireCasa();
                    ui.mover(proximaPosicao, this.getDirecao());
                    casaAtual = proximaCasa;
                    proximaPosicao = malha.getProximaPosicao(proximaPosicao, this.getDirecao());
                    proximaCasa = malha.getCasa(proximaPosicao);
                    sleep(2000);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}