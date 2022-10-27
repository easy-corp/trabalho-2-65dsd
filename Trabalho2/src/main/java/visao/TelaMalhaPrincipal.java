package visao;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class TelaMalhaPrincipal {

    public static final int size = 25;
	private final int width;
	private final int heigth;
    private HBox malha;
    private Group grupoMalha;
    private Group grupoCarros; 

    public TelaMalhaPrincipal(int width, int height) {
        this.width = width;
        this.heigth = height;

        this.grupoMalha = new Group();
        this.grupoCarros = new Group();
    }

    //O conteudo da tela
    public Pane createContent() {
        Pane root = new Pane();

        //Box principal
        this.malha = new HBox();
        this.malha.setPrefSize((width * size), (heigth * size));
        this.malha.setAlignment(Pos.CENTER);

        this.grupoMalha.getChildren().add(this.grupoCarros);
        
        this.malha.getChildren().add(this.grupoMalha);

        root.getChildren().addAll(this.malha);

        return root;
    }

    public int getSize() {
        return this.size;
    }

    public Group getGrupoMalha() {
        return this.grupoMalha;
    }

    public Group getGrupoCarros() {
        return this.grupoCarros;
    }

}
