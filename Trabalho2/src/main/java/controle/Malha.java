package controle;

import java.util.ArrayList;
import java.util.List;

import modelo.Casa;
import modelo.ui.UiCarro;

public class Malha {

    //Singleton guardando os dados importantes para o projeto

    private static Malha instance;
    private int width;                                             //Largura da malha
    private int height;                                            //Altura da malha
    private Casa[][] malha;                                        //A malha
    private List<int[]> posEntradas = new ArrayList<>();           //Lista das posições de entrada
    private List<int[]> posSaidas = new ArrayList<>();             //Lista das posições de saida
    private List<UiCarro> carros = new ArrayList<>();              //Lista dos carros inseridos

    private Malha() { }

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

    public void addPosEntrada(int[] entrada) {
        this.posEntradas.add(entrada);
    }

    public List<int[]> getPosEntradas() {
        return this.posEntradas;
    }

    public void addPosSaida(int[] saida) {
        this.posSaidas.add(saida);
    }

    public List<int[]> getPosSaidas() {
        return this.posSaidas;
    }

    public void addCarro(UiCarro carro) {
        this.carros.add(carro);
    }

    public List<UiCarro> getCarros() {
        return this.carros;
    }

}
