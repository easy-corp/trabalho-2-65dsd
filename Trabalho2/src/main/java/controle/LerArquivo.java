package controle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LerArquivo {
    
    private String[][] malha;
    private Map<Integer, String> padraoMalha;

    public LerArquivo(String caminho) throws IOException {
        //cria map de guia e le o arquivo
        padroes();
        ler(caminho); 
    }

    public void ler(String caminho) throws IOException {
        //Abre o arquivo
        File file = new File(caminho);
		String dados = new String(Files.readAllBytes(file.toPath()));

        //Define tamanho da malha de acordo com as duas primeiras linhas do arquivo
        this.malha = new String[Integer.parseInt(dados.split("\\r?\\n")[0])][Integer.parseInt(dados.split("\\r?\\n")[1])];

        //Percorrendo cada linha do arquivo
        //Ignoramos linhas 1 e 2 referentes ao tamanho da malha
        for (int i = 2; i < dados.split("\\r?\\n").length; i++) {
            //Percorrendo cada coluna do arquivo
            String linha = dados.split("\\r?\\n")[i];

            for (int n = 0; n < linha.split("	").length; n++) {
                //Recupera valor com base no Map de guia
                int celula = Integer.parseInt(linha.split("	")[n]);

                this.malha[i - 2][n] = this.padraoMalha.get(celula);
            }
        }
    }

    public void padroes() {
        this.padraoMalha = new HashMap<Integer, String>();

        //Para cada valor inteiro ha um desenho correspondente
        //C indica cruzamentos
        this.padraoMalha.put(0, "0");
        this.padraoMalha.put(1, "UP");
        this.padraoMalha.put(2, "RIGHT");
        this.padraoMalha.put(3, "DOWN");
        this.padraoMalha.put(4, "LEFT");
        this.padraoMalha.put(5, "C/UP");
        this.padraoMalha.put(6, "C/RIGHT");
        this.padraoMalha.put(7, "C/DOWN");
        this.padraoMalha.put(8, "C/LEFT");
        this.padraoMalha.put(9, "C/UP/RIGHT");
        this.padraoMalha.put(10, "C/UP/LEFT");
        this.padraoMalha.put(11, "C/DOWN/RIGHT");
        this.padraoMalha.put(12, "C/DOWN/LEFT");
    }

    public String[][] getMalha() {
        return this.malha;
    }

}
