import java.io.IOException;

import controle.ControladorMalha;
import javafx.application.Application;
import javafx.stage.Stage;

//mvn clean javafx:run -e -X
public class App extends Application {
    
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage inicial) throws Exception {
        new ControladorMalha();
    }
    
}
