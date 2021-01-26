package application;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class InicioController {
	@FXML private TableView <Libro> libros;
	@FXML private TableColumn isbn;
	@FXML private TableColumn titulo;
	@FXML private TableColumn descripcion;
	@FXML private TableColumn autor;
	@FXML private TableColumn editorial;
	@FXML private TableColumn fechaPublicacion;
	
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
