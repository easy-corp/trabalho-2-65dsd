package modelo;

import controle.Malha;
import modelo.ui.UiCarro;

public class Carro extends Thread {

    private int[] posicao;       //Posicionamento do carro, X e Y, respectivamente
    private String direcao;      //Direcao para onde o carro esta indo
    private UiCarro ui;          //UI do carro

    public Carro(int x, int y, String direcao) {
        mover(x, y, direcao);
    }

    public int[] getPosicao() {
        return this.posicao;
    }

    public void setDirecao(String direcao) {
        //Se o carro estiver em um cruzamento, nao vamos alterar a sua posicao
        //Cruzamentos possuem C em sua descricao na Malha
        if (!direcao.contains("C")) {
            this.direcao = direcao;
        }
    }

    public String getDirecao() {
        return this.direcao;
    }

    //Movimento do carro
    public void mover(int x, int y, String direcao) {
        this.posicao = new int[] {x, y};
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
            int[] p = malha.getProximaCasa(this.getPosicao()[0], this.getPosicao()[1], this.getDirecao());
            Casa casa = malha.getMalha()[p[0]][p[1]];

            while (!casa.getSimbolo().contains("C")) {
                ui.mover(p[0], p[1], this.getDirecao());
                p = malha.getProximaCasa(p[0], p[1], this.getDirecao());
                casa = malha.getMalha()[p[0]][p[1]];

                sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Deu ruim");
        }
    }

}