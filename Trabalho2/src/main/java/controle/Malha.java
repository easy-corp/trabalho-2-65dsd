package controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import modelo.Carro;
import modelo.Casa;
import modelo.Casa.TipoCasa;
import modelo.ui.UiCarro;
import visao.TelaMalhaPrincipal;

public class Malha {

    // Singleton guardando os dados importantes para o projeto

    private static Malha instance; // Instancia do Singleton
    private TelaMalhaPrincipal telaMalha; // Tela principal
    private int width; // Largura da malha
    private int height; // Altura da malha
    private Casa[][] malha; // A malha
    private List<Point2D> posEntradas = new ArrayList<>(); // Lista das posições de entrada
    private List<Point2D> posSaidas = new ArrayList<>(); // Lista das posições de saida
    private List<UiCarro> carros = new ArrayList<>(); // Lista dos carros inseridos
    private List<UiCarro> carrosAtivos = new ArrayList<>(); // Lista dos carros inseridos
    private List<Casa> cruzamentos = new ArrayList<>(); // Lista dos carros inseridos
    private HashMap<TipoCasa, List<List<TipoCasa>>> movimentosPossiveisCruzamento = this
            .criaMovimentosPossiveisCruzamento(); // Movimentos possiveis quando se esta em um cruzamento
    private HashMap<Point2D, UiCarro> casaOcupada = new HashMap<>(); // Controle de ocupacao de casa
    private IntegerProperty contadorCarros = new SimpleIntegerProperty(); // Contador de carros (listener)
    private int qtdCarros = 0; // Quantidade de carros
    private String tipoThread = "Semáforo"; // Tipo dos controladoes (Semáforo ou Monitor)
    private boolean destroy = false; // Controle para destruir threads
    private boolean debugMode = false; // Controle para visualizar acquire e realease

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

    public Casa getCasa(Point2D where) {
        return this.malha[(int) where.getX()][(int) where.getY()];
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

    public List<UiCarro> getCarrosAtivos() {
        return carrosAtivos;
    }

    public void setCarrosAtivos(List<UiCarro> carrosAtivos) {
        this.carrosAtivos = carrosAtivos;
    }

    public int getQtdCarros() {
        return qtdCarros;
    }

    public void setQtdCarros(int qtdCarros) {
        this.qtdCarros = qtdCarros;
    }

    public void setTelaMalha(TelaMalhaPrincipal tela) {
        this.telaMalha = tela;
    }

    public void addCarroAtivo(UiCarro carro) {
        this.carrosAtivos.add(carro);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                contadorCarros.set(carrosAtivos.size());
            }

        });
    }

    public void removeCarro(UiCarro carro) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                carros.remove(carro);
                carrosAtivos.remove(carro);
                contadorCarros.set(carrosAtivos.size());

                if(!destroy){
                    Carro newCarro = spawnarCarro();
                    newCarro.start();
                }
            }

        });

    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public String getTipoThread() {
        return this.tipoThread;
    }

    public void setTipoThread(String text) {
        this.tipoThread = text;
    }

    public boolean usaSemaforo() {
        if (getTipoThread().contentEquals("Semáforo")) {
            return true;
        } else {
            return false;
        }
    }

    public List<UiCarro> getCarros() {
        return this.carros;
    }

    public void clearCarros(){
        this.carros = new ArrayList<>();
        this.carrosAtivos = new ArrayList<>();
    }

    public Casa getProximaCasa(Point2D posicaoAtual, TipoCasa direcao) {
        return this.getCasa(this.getProximaPosicao(posicaoAtual, direcao));
    }

    public void addCruzamento(Casa casa) {
        this.cruzamentos.add(casa);
    }

    public List<Casa> getCruzamento() {
        return this.cruzamentos;
    }

    public IntegerProperty getContadorCarros() {
        return this.contadorCarros;
    }

    public void setDebug(boolean opt) {
        this.debugMode = opt;
    }

    public boolean getDebug() {
        return this.debugMode;
    }

    /**
     * Retorna a proxima posição para um carro na malha
     * 
     * @param posicaoAtual
     * @param direcao
     * @return Point2D
     */
    public Point2D getProximaPosicao(Point2D posicaoAtual, TipoCasa direcao) {
        switch (direcao) {
            case TIPO_UP:
            case TIPO_CRUZAMENTO_UP:
                return new Point2D(posicaoAtual.getX() - 1, posicaoAtual.getY());
            case TIPO_DOWN:
            case TIPO_CRUZAMENTO_DOWN:
                return new Point2D(posicaoAtual.getX() + 1, posicaoAtual.getY());
            case TIPO_LEFT:
            case TIPO_CRUZAMENTO_LEFT:
                return new Point2D(posicaoAtual.getX(), posicaoAtual.getY() - 1);
            case TIPO_RIGHT:
            case TIPO_CRUZAMENTO_RIGHT:
                return new Point2D(posicaoAtual.getX(), posicaoAtual.getY() + 1);
            default:
                return new Point2D(0, 0);
        }
    }

    private HashMap<TipoCasa, List<List<TipoCasa>>> criaMovimentosPossiveisCruzamento() {
        HashMap<TipoCasa, List<List<TipoCasa>>> listaMovimentos = new HashMap<TipoCasa, List<List<TipoCasa>>>();

        // Vindo da direita para esquerda
        List<List<TipoCasa>> movimentosLeft = new ArrayList<>();

        List<TipoCasa> movimentosLeft1 = new ArrayList<>();
        movimentosLeft1.add(TipoCasa.TIPO_LEFT);
        movimentosLeft1.add(TipoCasa.TIPO_LEFT);
        movimentosLeft1.add(TipoCasa.TIPO_LEFT);
        movimentosLeft.add(movimentosLeft1);

        List<TipoCasa> movimentosLeft2 = new ArrayList<>();
        movimentosLeft2.add(TipoCasa.TIPO_LEFT);
        movimentosLeft2.add(TipoCasa.TIPO_UP);
        movimentosLeft.add(movimentosLeft2);

        List<TipoCasa> movimentosLeft3 = new ArrayList<>();
        movimentosLeft3.add(TipoCasa.TIPO_LEFT);
        movimentosLeft3.add(TipoCasa.TIPO_LEFT);
        movimentosLeft3.add(TipoCasa.TIPO_DOWN);
        movimentosLeft3.add(TipoCasa.TIPO_DOWN);
        movimentosLeft.add(movimentosLeft3);

        listaMovimentos.put(TipoCasa.TIPO_LEFT, movimentosLeft);

        // Vindo da esquerda para direita
        List<List<TipoCasa>> movimentosRight = new ArrayList<>();

        List<TipoCasa> movimentosRight1 = new ArrayList<>();
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight.add(movimentosRight1);

        List<TipoCasa> movimentosRight2 = new ArrayList<>();
        movimentosRight2.add(TipoCasa.TIPO_RIGHT);
        movimentosRight2.add(TipoCasa.TIPO_DOWN);
        movimentosRight.add(movimentosRight2);

        List<TipoCasa> movimentosRight3 = new ArrayList<>();
        movimentosRight3.add(TipoCasa.TIPO_RIGHT);
        movimentosRight3.add(TipoCasa.TIPO_RIGHT);
        movimentosRight3.add(TipoCasa.TIPO_UP);
        movimentosRight3.add(TipoCasa.TIPO_UP);
        movimentosRight.add(movimentosRight3);

        listaMovimentos.put(TipoCasa.TIPO_RIGHT, movimentosRight);

        // Vindo de baixo para cima
        List<List<TipoCasa>> movimentosUp = new ArrayList<>();

        List<TipoCasa> movimentosUp1 = new ArrayList<>();
        movimentosUp1.add(TipoCasa.TIPO_UP);
        movimentosUp1.add(TipoCasa.TIPO_UP);
        movimentosUp1.add(TipoCasa.TIPO_UP);
        movimentosUp.add(movimentosUp1);

        List<TipoCasa> movimentosUp2 = new ArrayList<>();
        movimentosUp2.add(TipoCasa.TIPO_UP);
        movimentosUp2.add(TipoCasa.TIPO_RIGHT);
        movimentosUp.add(movimentosUp2);

        List<TipoCasa> movimentosUp3 = new ArrayList<>();
        movimentosUp3.add(TipoCasa.TIPO_UP);
        movimentosUp3.add(TipoCasa.TIPO_UP);
        movimentosUp3.add(TipoCasa.TIPO_LEFT);
        movimentosUp3.add(TipoCasa.TIPO_LEFT);
        movimentosUp.add(movimentosUp3);

        listaMovimentos.put(TipoCasa.TIPO_UP, movimentosUp);

        // Vindo de cima para baixo
        List<List<TipoCasa>> movimentosDown = new ArrayList<>();

        List<TipoCasa> movimentosDown1 = new ArrayList<>();
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        movimentosDown.add(movimentosDown1);

        List<TipoCasa> movimentosDown2 = new ArrayList<>();
        movimentosDown2.add(TipoCasa.TIPO_DOWN);
        movimentosDown2.add(TipoCasa.TIPO_LEFT);
        movimentosDown.add(movimentosDown2);

        List<TipoCasa> movimentosDown3 = new ArrayList<>();
        movimentosDown3.add(TipoCasa.TIPO_DOWN);
        movimentosDown3.add(TipoCasa.TIPO_DOWN);
        movimentosDown3.add(TipoCasa.TIPO_RIGHT);
        movimentosDown3.add(TipoCasa.TIPO_RIGHT);
        movimentosDown.add(movimentosDown3);

        listaMovimentos.put(TipoCasa.TIPO_DOWN, movimentosDown);

        return listaMovimentos;
    }

    public HashMap<TipoCasa, List<List<TipoCasa>>> getMovimentosPossiveisCruzamento() {
        return this.movimentosPossiveisCruzamento;
    }   

    public Carro spawnarCarro() {
        Random random = new Random();
        // Recupera uma posicao aleatoria dentre as entradas
        int posSpawn = random.nextInt(getPosEntradas().size());

        Point2D posAleatoria = getPosEntradas().get(posSpawn);

        // Recupera a posicao a qual o carro esta virado
        modelo.Casa.TipoCasa direcao = getCasa(posAleatoria).getTipo();

        // Cria o carro
        Carro carro = new Carro(posAleatoria, direcao);
        UiCarro uiCarro = new UiCarro(carro, TelaMalhaPrincipal.size);

        // Adiciona o carro na tela
        this.telaMalha.getGrupoCarros().getChildren().add(uiCarro);

        // Adiciona o carro na lista para manipular
        addCarro(uiCarro);

        return carro;
        // inicia a thread do carro
        // carro.start();
    }

    public synchronized boolean trataCasa(List<Point2D> pontos, UiCarro ui, boolean ocupa) {
        if (ocupa) {
            // Verifica se todas as posicoes de destino estao livres
            for (Point2D d : pontos) {
                if (casaOcupada.containsKey(d)) {
                    return false;
                }
            }

            // Ocupa todas as posicoes
            for (Point2D d : pontos) {
                casaOcupada.put(d, ui);
            }

            return true;
        }

        for (Point2D o : pontos) {
            casaOcupada.remove(o);
        }

        return true;
    }

    public List<Point2D> getCasaCruzamento(Point2D posicaoAtual, TipoCasa direcao) {
        List<Point2D> pos = new ArrayList<>();
        switch (direcao) {
            case TIPO_DOWN:
                Point2D baixo = getProximaPosicao(posicaoAtual, TipoCasa.TIPO_DOWN);
                Point2D baixoBaixo = getProximaPosicao(baixo, TipoCasa.TIPO_DOWN);
                Point2D baixoDir = getProximaPosicao(baixo, TipoCasa.TIPO_RIGHT);
                Point2D baixoBaixoDir = getProximaPosicao(baixoBaixo, TipoCasa.TIPO_RIGHT);
                pos.add(baixo);
                pos.add(baixoBaixo);
                pos.add(baixoDir);
                pos.add(baixoBaixoDir);
                break;
            case TIPO_UP:
                Point2D cima = getProximaPosicao(posicaoAtual, TipoCasa.TIPO_UP);
                Point2D cimaCima = getProximaPosicao(cima, TipoCasa.TIPO_UP);
                Point2D cimaEsq = getProximaPosicao(cima, TipoCasa.TIPO_LEFT);
                Point2D cimaCimaEsq = getProximaPosicao(cimaCima, TipoCasa.TIPO_LEFT);
                pos.add(cima);
                pos.add(cimaCima);
                pos.add(cimaEsq);
                pos.add(cimaCimaEsq);
                break;
            case TIPO_LEFT:
                Point2D esq = getProximaPosicao(posicaoAtual, TipoCasa.TIPO_LEFT);
                Point2D esqEsq = getProximaPosicao(esq, TipoCasa.TIPO_LEFT);
                Point2D esqBaixo = getProximaPosicao(esq, TipoCasa.TIPO_DOWN);
                Point2D esqEsqBaixo = getProximaPosicao(esqEsq, TipoCasa.TIPO_DOWN);
                pos.add(esq);
                pos.add(esqEsq);
                pos.add(esqBaixo);
                pos.add(esqEsqBaixo);
                break;
            case TIPO_RIGHT:
                Point2D dir = getProximaPosicao(posicaoAtual, TipoCasa.TIPO_RIGHT);
                Point2D dirDir = getProximaPosicao(dir, TipoCasa.TIPO_RIGHT);
                Point2D dirCima = getProximaPosicao(dir, TipoCasa.TIPO_UP);
                Point2D dirDirCima = getProximaPosicao(dirDir, TipoCasa.TIPO_UP);
                pos.add(dir);
                pos.add(dirDir);
                pos.add(dirCima);
                pos.add(dirDirCima);
                break;
        }

        return pos;
    }

}