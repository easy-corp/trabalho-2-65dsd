import java.io.IOException;

import control.LerArquivo;

public class App {
    
    public static void main(String[] args) throws IOException {
        LerArquivo leitor = new LerArquivo("../recursos/malha-exemplo-3.txt");

        for (int i = 0; i < leitor.getMalha().length; i++) {
            for (int n = 0; n < leitor.getMalha()[i].length; n++) {
                System.out.print(leitor.getMalha()[i][n] + " - ");
            }
            
            System.out.println();
        }
    }
    
}
