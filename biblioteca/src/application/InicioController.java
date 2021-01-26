package application;


import javafx.event.ActionEvent;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InicioController {
	@FXML private TableView <Libro> libros;
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

	public void crearLibro (String isbn, String titulo, String descripcion, String autor, String nombreEditorial, String fecha) {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");
			
			CodecRegistry pojoCodec = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			db = db.withCodecRegistry(pojoCodec);
			MongoCollection<Libro> collection = db.getCollection("libros", Libro.class);
			
			Libro libro = new Libro(isbn, titulo, descripcion, autor, new Editorial(nombreEditorial, fecha));
			collection.insertOne(libro);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(mongo != null) {
				mongo.close();
			}
		}
		
		
		
	}

}
