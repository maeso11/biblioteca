package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InicioController {
	@FXML private TableView <Libros> libros;
	@FXML private TableColumn isbn;
	@FXML private TableColumn titulo;
	@FXML private TableColumn descripcion;
	@FXML private TableColumn autor;
	@FXML private TableColumn editorial;
	@FXML private TableColumn fechaPublicacion;
	
	
	
	
	public void buscar(ActionEvent e) {
		Stage primaryStage=new Stage();
		((Node) e.getSource()).getScene().getWindow().hide();
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Buscar.fxml"));
			Scene scene = new Scene(root,900,670);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception v) {
			v.printStackTrace();
		}
}
}
