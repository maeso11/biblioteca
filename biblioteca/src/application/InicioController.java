package application;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InicioController {
	@FXML private TableView <Libros> libros;
	@FXML private TableColumn isbn;
	@FXML private TableColumn titulo;
	@FXML private TableColumn descripcion;
	@FXML private TableColumn autor;
	@FXML private TableColumn editorial;
	@FXML private TableColumn fechaPublicacion;
}
