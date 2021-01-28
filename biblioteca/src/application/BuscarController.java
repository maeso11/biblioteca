package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BuscarController {
	
	@FXML
	ComboBox<String> comboBox;
	
	@FXML
	public void initialize() {
		comboBox.getItems().addAll(
	            "ISBN",
	            "AUTOR",
	            "EDITORIAL",
	            "FECHA DE PUBLICACIÓN",
	            "TITULO"
	        );
	}
	
	public void btnFiltrar(ActionEvent event) {
		try {
			Stage primaryStage= new Stage();
			((Node) event.getSource()).getScene().getWindow().hide();
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Inicio.fxml"));
			Scene scene = new Scene(root,900,670);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	
}
