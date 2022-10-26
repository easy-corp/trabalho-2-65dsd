package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import controle.Malha;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;

public class Carro extends Thread {

    private Point2D posicao; // Posicionamento do carro, X e Y, respectivamente
    private TipoCasa direcao; // Direcao para onde o carro esta indo
    private UiCarro ui; // UI do carro
    private Malha malha;
    private HashMap<TipoCasa, List<List<TipoCasa>>> listaMovimentos;
    private Random random = new Random();

    public Carro(Point2D spawnPoint, TipoCasa direcao) {
        mover(spawnPoint, direcao);

        this.malha = Malha.getInstance();
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
            Malha malha = Malha.getInstance();
            Casa casaAtual = malha.getCasa(this.getPosicao());
            Point2D proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
            Casa proximaCasa = malha.getCasa(proximaPosicao);
            Semaphore cruzamentoAtual = null;

            casaAtual.acquireCasa();
            sleep(1000);

            while (true) {
                //Se estiver em frente de um cruzamento
                if (malha.getCruzamento().contains(proximaCasa)) {
                    //Casa antes de entrar no cruzamento;
                    Casa casaInicial = casaAtual;

                    //Dentro desse metodo ha um tryacquires
                    proximaCasa.acquireCruzamento();
                    cruzamentoAtual = proximaCasa.getMutexCruzamento();
                    
                    //Define qual dos caminhos seguir dentro do cruzamento
                    Point2D posAt = proximaPosicao;
                    Casa casaAt = malha.getCasa(posAt);
                    List<TipoCasa> movimentosAFazer = new ArrayList<>();

                    while(malha.getCruzamento().contains(casaAt) || casaAt.getTipo().name().contains("EMPTY")){
                        posAt = proximaPosicao;
                        casaAt = malha.getCasa(posAt);

                        List<List<TipoCasa>> movimentos = malha.getMovimentosPossiveisCruzamento().get(this.getDirecao());
                        int randomIndex = this.random.nextInt(movimentos.size());
                        movimentosAFazer = movimentos.get(randomIndex);
                        
                        for (int i = 1; i < movimentosAFazer.size(); i++) {
                            TipoCasa dir = movimentosAFazer.get(i);
                            posAt = malha.getProximaPosicao(posAt, dir);

                            casaAt = malha.getCasa(posAt);
                        }

                        casaAt = malha.getCasa(posAt);
                    }

                    //Faz Acquire na casa de saida
                    casaAtual = casaAt;
                    casaAtual.acquireCasa();

                    //Faz movimento ao longo do cruzamento
                    Point2D proximaPosicaoMovimento = null;
                    
                    for(TipoCasa m : movimentosAFazer){
                        proximaPosicaoMovimento = malha.getProximaPosicao(this.getPosicao(), m);
                        Casa casaMover = malha.getCasa(proximaPosicaoMovimento);

                        //Entrou no cruzamento
                        ui.mover(proximaPosicaoMovimento, casaMover.getTipo());
                        Thread.sleep(300);
                        casaInicial.releaseCasa();
                    }

                    proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
                    proximaCasa = malha.getCasa(proximaPosicao);
                    
                    casaAtual.releaseCasa();
                    cruzamentoAtual.release();             
                } else {
                    proximaCasa.acquireCasa();
                    casaAtual.releaseCasa();

                    if (!verificaFim(this, proximaPosicao)) {
                        ui.mover(proximaPosicao, this.getDirecao());
                        casaAtual = proximaCasa;
                        proximaPosicao = malha.getProximaPosicao(proximaPosicao, this.getDirecao());
                        proximaCasa = malha.getCasa(proximaPosicao);
                    } else {
                        proximaCasa.releaseCasa();

                        return;
                    }
                    
                    sleep(100);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean verificaFim(Carro carro, Point2D posicao) throws InterruptedException {
        if (this.malha.getPosSaidas().contains(posicao)) {
            this.ui.finalizarCarro();
            this.malha.getCarros().remove(this.getUi());
            carro = null;

            return true;
        }
        
        return false;
    }

}