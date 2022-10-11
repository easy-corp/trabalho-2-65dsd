package controle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;
import modelo.Casa;

public class LerArquivo {
    
    private int width;
    private int height;
    private Casa[][] malha;
    private Map<Integer, Casa> padraoMalha;
    private List<int[]> posEntradas;
    private List<int[]> posSaidas;

    public LerArquivo(String caminho) throws IOException {
        this.posEntradas = new ArrayList<>();
        this.posSaidas = new ArrayList<>();

        //cria map de guia e le o arquivo
        padroes();
        ler(caminho); 
    }

    public void ler(String caminho) throws IOException {
        //Abre o arquivo
        File file = new File(caminho);
		String dados = new String(Files.readAllBytes(file.toPath()));

        //Define tamanho da malha de acordo com as duas primeiras linhas do arquivo
        this.width = Integer.parseInt(dados.split("\\r?\\n")[0]);
        this.height = Integer.parseInt(dados.split("\\r?\\n")[1]);
        this.malha = new Casa[this.width][this.height];

        //Percorrendo cada linha do arquivo
        //Ignoramos linhas 1 e 2 referentes ao tamanho da malha
        for (int x = 2; x < dados.split("\\r?\\n").length; x++) {
            //Percorrendo cada coluna do arquivo
            String dadosLinha = dados.split("\\r?\\n")[x];

            //Delimitando as duas linhas puladas no inicio
            int linha = x - 2;

            for (int y = 0; y < dadosLinha.split("	").length; y++) {
                int coluna = y;

                //Recupera valor com base no Map de guia
                int celula = Integer.parseInt(dadosLinha.split("	")[coluna]);

                this.malha[linha][coluna] = this.padraoMalha.get(celula);

                //Verifica entradas
                if (coluna == 0 && celula == 2 ||
                    coluna == (this.height - 1) && celula == 4 ||
                    linha == 0 && celula == 3 ||
                    linha == (this.width - 1) && celula == 1) {
                        this.posEntradas.add(new int[] {linha, coluna});
                }

                //Verifica saidas
                if (coluna == 0 && celula == 4 ||
                    coluna == (this.height - 1) && celula == 2 ||
                    linha == 0 && celula == 1 ||
                    linha == (this.width - 1) && celula == 3) {
                        this.posSaidas.add(new int[] {linha, coluna});
                }
            }
        }
    }

    public void padroes() {
        this.padraoMalha = new HashMap<Integer, Casa>();

        //Para cada valor inteiro ha um desenho correspondente
        this.padraoMalha.put(0, new Casa(Color.WHITE, "0"));
        this.padraoMalha.put(1, new Casa(Color.YELLOW, "UP"));
        this.padraoMalha.put(2, new Casa(Color.LIGHTBLUE, "RIGHT"));
        this.padraoMalha.put(3, new Casa(Color.LIGHTGREEN, "DOWN"));
        this.padraoMalha.put(4, new Casa(Color.LIGHTSALMON, "LEFT"));
        this.padraoMalha.put(5, new Casa(Color.VIOLET, "C-UP"));
        this.padraoMalha.put(6, new Casa(Color.VIOLET, "C-RIGHT"));
        this.padraoMalha.put(7, new Casa(Color.VIOLET, "C-DOWN"));
        this.padraoMalha.put(8, new Casa(Color.VIOLET, "C-LEFT"));
        this.padraoMalha.put(9, new Casa(Color.VIOLET, "C-UP-RIGHT"));
        this.padraoMalha.put(10, new Casa(Color.VIOLET, "C-UP-LEFT"));
        this.padraoMalha.put(11, new Casa(Color.VIOLET, "C-DOWN-RIGHT"));
        this.padraoMalha.put(12, new Casa(Color.VIOLET, "C-DOWN-LEFT"));
    }

    public Casa[][] getMalha() {
        return this.malha;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public List<int[]> getEntradas() {
        return this.posEntradas;
    }

    public List<int[]> getSaidas() {
        return this.posSaidas;
    }

}
