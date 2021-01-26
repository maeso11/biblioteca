package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
}
