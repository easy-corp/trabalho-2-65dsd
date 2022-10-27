package modelo;

import java.util.ArrayList;
import java.util.Date;
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
    private Malha malha;
    private HashMap<TipoCasa, List<List<TipoCasa>>> listaMovimentos;
    private Random random = new Random();
    private int velocidade;
    private boolean semaforo = true;
    private boolean firstCasa = true;
    private boolean destroy = false;

    public Carro(Point2D spawnPoint, TipoCasa direcao) {
        mover(spawnPoint, direcao);

        velocidade = random.nextInt(10) * 100;
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

    

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    @Override
    public void run() {
        try {
            Malha malha = Malha.getInstance();
            Casa casaAtual = malha.getCasa(this.getPosicao());
            Point2D proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
            Casa proximaCasa = malha.getCasa(proximaPosicao);
            Semaphore cruzamentoAtual = null;

            if (semaforo) {
                // Semaforo faz acquire
                casaAtual.acquireCasa();
                if (firstCasa) {
                    firstCasa = false;
                    malha.addCarroAtivo(this.ui);
                }
            } else {
                // Senao tenta ocupar a casa
                List<Point2D> casaAtualLista = new ArrayList<>();
                casaAtualLista.add(casaAtual.getUi().getPosicao());
                boolean ocupou = malha.ocupaCasa(casaAtualLista, ui);
                while (!ocupou) {
                    sleep(velocidade);
                    ocupou = malha.ocupaCasa(casaAtualLista, ui);
                }
            }

            sleep(velocidade);

            while (!destroy) {
                System.out.println(new Date().getTime());
                // Se estiver em frente de um cruzamento
                if (malha.getCruzamento().contains(proximaCasa)) {
                    // Casa antes de entrar no cruzamento;
                    Casa casaInicial = casaAtual;

                    // Define qual dos caminhos seguir dentro do cruzamento
                    Point2D posAt = proximaPosicao;
                    Casa casaAt = malha.getCasa(posAt);
                    List<TipoCasa> movimentosAFazer = new ArrayList<>();

                    while (malha.getCruzamento().contains(casaAt) || casaAt.getTipo().name().contains("EMPTY")) {
                        posAt = proximaPosicao;
                        casaAt = malha.getCasa(posAt);

                        List<List<TipoCasa>> movimentos = malha.getMovimentosPossiveisCruzamento()
                                .get(this.getDirecao());
                        int randomIndex = this.random.nextInt(movimentos.size());
                        movimentosAFazer = movimentos.get(randomIndex);

                        for (int i = 1; i < movimentosAFazer.size(); i++) {
                            TipoCasa dir = movimentosAFazer.get(i);
                            posAt = malha.getProximaPosicao(posAt, dir);

                            casaAt = malha.getCasa(posAt);
                        }

                        casaAt = malha.getCasa(posAt);
                    }

                    List<Point2D> casasCruzamento = new ArrayList<>();

                    if (semaforo) {
                        // Dentro desse metodo ha um tryacquires
                        proximaCasa.acquireCruzamento();
                        cruzamentoAtual = proximaCasa.getMutexCruzamento();

                        // Faz Acquire na casa de saida
                        casaAtual = casaAt;
                        casaAtual.acquireCasa();
                    } else {
                        // Cria uma lista com as posicoes ate sair do cruzamento
                        casasCruzamento.add(proximaPosicao);
                        for (int i = 1; i <= movimentosAFazer.size(); i++) {
                            TipoCasa direcao = movimentosAFazer.get(i);
                            casasCruzamento.add(malha.getProximaPosicao(posAt, direcao));
                        }

                        // Tenta ocupar todas as casas necessarias
                        boolean ocupou = malha.ocupaCasa(casasCruzamento, ui);
                        if (!ocupou) {
                            sleep(velocidade);
                            ocupou = malha.ocupaCasa(casasCruzamento, ui);
                        }
                    }

                    // Faz movimento ao longo do cruzamento
                    Point2D proximaPosicaoMovimento = null;

                    boolean primeiraCasaCruzamento = true;
                    for (TipoCasa m : movimentosAFazer) {
                        proximaPosicaoMovimento = malha.getProximaPosicao(this.getPosicao(), m);
                        Casa casaMover = malha.getCasa(proximaPosicaoMovimento);

                        // Entrou no cruzamento
                        ui.mover(proximaPosicaoMovimento, casaMover.getTipo());

                        // Quando entrar no cruzamento libera a casa que tava
                        if (primeiraCasaCruzamento) {
                            if (semaforo) {
                                casaInicial.releaseCasa();
                            } else {
                                List<Point2D> casaLiberar = new ArrayList<>();
                                casaLiberar.add(casaMover.getUi().getPosicao());
                                malha.liberaCasa(casaLiberar);
                            }

                            primeiraCasaCruzamento = false;
                        }
                        Thread.sleep(velocidade);
                    }

                    proximaPosicao = malha.getProximaPosicao(this.getPosicao(), this.getDirecao());
                    proximaCasa = malha.getCasa(proximaPosicao);

                    if (semaforo) {
                        cruzamentoAtual.release();
                    } else {
                        malha.liberaCasa(casasCruzamento);
                    }
                } else {
                    if (semaforo) {
                        proximaCasa.acquireCasa();
                        casaAtual.releaseCasa();
                    } else {
                        List<Point2D> proximaCasaPoint = new ArrayList<>();
                        proximaCasaPoint.add(proximaPosicao);

                        boolean ocupou = malha.ocupaCasa(proximaCasaPoint, ui);
                        while (!ocupou) {
                            sleep(velocidade);
                            malha.ocupaCasa(proximaCasaPoint, ui);
                        }
                    }
                    
                    ui.mover(proximaPosicao, this.getDirecao());
                    sleep(velocidade);
                    if (!verificaFim(proximaPosicao)) {
                        casaAtual = proximaCasa;
                        proximaPosicao = malha.getProximaPosicao(proximaPosicao, this.getDirecao());
                        proximaCasa = malha.getCasa(proximaPosicao);
                    } else {
                        if (semaforo) {
                            proximaCasa.releaseCasa();
                        } else {
                            List<Point2D> proximaCasaPoint = new ArrayList<>();
                            proximaCasaPoint.add(proximaPosicao);
                            malha.liberaCasa(proximaCasaPoint);
                        }

                        return;
                    }
                    sleep(velocidade);
                }
            }


            if(cruzamentoAtual != null){
                cruzamentoAtual.release();
            }

            casaAtual.releaseCasa();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verificaFim(Point2D posicao) throws InterruptedException {
        for(Point2D p : this.malha.getPosSaidas()){
            if(p.getX() == posicao.getX() && p.getY() == posicao.getY()){
                destruirCarro();
                return true;
            }
        }

        return false;
    }

    public void destruirCarro() {
        this.ui.finalizarCarro();
        this.malha.removeCarro(this.ui);
    }

}