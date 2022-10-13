package modelo;

public class Carro {
    
    private int[] posicao;       //Posicionamento do carro, X e Y, respectivamente
    private String direcao;      //Direcao para onde o carro esta indo

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

}
