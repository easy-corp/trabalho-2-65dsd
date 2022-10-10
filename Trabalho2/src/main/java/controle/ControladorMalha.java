package controle;

import java.io.IOException;

public class ControladorMalha {
    
    public ControladorMalha() throws IOException {
        LerArquivo leitor = new LerArquivo("recursos/malha-exemplo-2.txt");

        for (int i = 0; i < leitor.getMalha().length; i++) {
            for (int n = 0; n < leitor.getMalha()[i].length; n++) {
                System.out.print(leitor.getMalha()[i][n] + " - ");
            }
            
            System.out.println();
        }
    }    

}
