package controle;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<Casa> cruzamentos = new ArrayList<>(); // Lista dos carros inseridos
    private HashMap<TipoCasa, List<List<TipoCasa>>> movimentosPossiveisCruzamento = this.criaMovimentosPossiveisCruzamento();

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

    public void addCruzamento(Casa casa) {
        this.cruzamentos.add(casa);
    }

    public List<Casa> getCruzamento() {
        return this.cruzamentos;
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

    private HashMap<TipoCasa, List<List<TipoCasa>>> criaMovimentosPossiveisCruzamento(){
        HashMap<TipoCasa, List<List<TipoCasa>>> listaMovimentos = new HashMap<TipoCasa, List<List<TipoCasa>>>();
        
        //Vindo da direita para esquerda
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

        //Vindo da esquerda para direita
        List<List<TipoCasa>> movimentosRight = new ArrayList<>();
        
        List<TipoCasa> movimentosRight1 = new ArrayList<>();
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight1.add(TipoCasa.TIPO_RIGHT);
        movimentosRight.add(movimentosRight1);

        List<TipoCasa> movimentosRight2 = new ArrayList<>();
        movimentosRight2.add(TipoCasa.TIPO_RIGHT);
        movimentosRight2.add(TipoCasa.TIPO_DOWN);
        movimentosRight.add(movimentosLeft2);

        List<TipoCasa> movimentosRight3 = new ArrayList<>();
        movimentosRight3.add(TipoCasa.TIPO_RIGHT);
        movimentosRight3.add(TipoCasa.TIPO_RIGHT);
        movimentosRight3.add(TipoCasa.TIPO_UP);
        movimentosRight3.add(TipoCasa.TIPO_UP);
        movimentosRight.add(movimentosRight3);

        listaMovimentos.put(TipoCasa.TIPO_RIGHT, movimentosRight);

        //Vindo de baixo para cima
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
        
        //Vindo de cima para baixo
        List<List<TipoCasa>> movimentosDown = new ArrayList<>();
        
        List<TipoCasa> movimentosDown1 = new ArrayList<>();
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        movimentosDown1.add(TipoCasa.TIPO_DOWN);
        // movimentosDown.add(movimentosDown1);

        List<TipoCasa> movimentosDown2 = new ArrayList<>();
        movimentosDown2.add(TipoCasa.TIPO_DOWN);
        movimentosDown2.add(TipoCasa.TIPO_LEFT);
        // movimentosDown.add(movimentosDown2);

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

}
