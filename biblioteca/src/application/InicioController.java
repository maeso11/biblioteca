package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InicioController {
	@FXML
	private TableView<Libro> tabla;
	@FXML
	private TableColumn isbn;
	@FXML
	private TableColumn titulo;
	@FXML
	private TableColumn descripcion;
	@FXML
	private TableColumn autor;
	@FXML
	private TableColumn<Libro, Editorial> editorial;
	@FXML
	private TableColumn<Libro, Editorial> fechaPublicacion;

	private ObservableList<Libro> oLibro;
	private ObservableList<Editorial> oEditorial;

	public void buscar(ActionEvent e) {
		Stage primaryStage = new Stage();
		((Node) e.getSource()).getScene().getWindow().hide();
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Buscar.fxml"));
			Scene scene = new Scene(root, 900, 670);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception v) {
			v.printStackTrace();
		}
	}

	/*
	 * public void crearLibro (String isbn, String titulo, String descripcion,
	 * String autor, String nombreEditorial, String fecha) { MongoClient mongo =
	 * null; try { mongo = MongoClients.create(); MongoDatabase db =
	 * mongo.getDatabase("biblioteca");
	 * 
	 * CodecRegistry pojoCodec =
	 * CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
	 * CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).
	 * build())); db = db.withCodecRegistry(pojoCodec); MongoCollection<Libro>
	 * collection = db.getCollection("libros", Libro.class);
	 * 
	 * Libro libro = new Libro(isbn, titulo, descripcion, autor, new
	 * Editorial(nombreEditorial, fecha)); collection.insertOne(libro);
	 * }catch(Exception e) { e.printStackTrace(); }finally { if(mongo != null) {
	 * mongo.close(); } } }
	 */
	public void eliminarLibro(String isbn) {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");

			CodecRegistry pojoCodec = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			db = db.withCodecRegistry(pojoCodec);
			MongoCollection<Libro> collection = db.getCollection("libros", Libro.class);

			collection.deleteOne(new Document("isbn", isbn));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	public void actualizarLibro(String titulo, String tituloNuevo, String descripcionNuevo, String autorNuevo,
			String nombreEditorialNuevo, String fechaNuevo) {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");

			db.getCollection("libros").updateOne(new Document("titulo", titulo),
					new Document("$set",
							new Document("editorial.nombre", nombreEditorialNuevo).append("titulo", tituloNuevo)
									.append("autor", autorNuevo).append("descripcion", descripcionNuevo)
									.append("editorial.fecha_publicacion", fechaNuevo)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	@FXML
	public void initialize() {
		this.isbn.setCellValueFactory(new PropertyValueFactory("isbn"));
		this.titulo.setCellValueFactory(new PropertyValueFactory("titulo"));
		this.autor.setCellValueFactory(new PropertyValueFactory("autor"));
		this.fechaPublicacion.setCellValueFactory(new PropertyValueFactory("editorial"));
		this.descripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
		this.editorial.setCellValueFactory(new PropertyValueFactory("nombre"));
		
		MongoClient mongoClient = null;
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("biblioteca");

			// configurar codecRegistry para usar POJOs
			CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
					MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

			// MongoCollection instance configured with the Pojo
			database = database.withCodecRegistry(pojoCodecRegistry);
			MongoCollection<Libro> collection = database.getCollection("libros", Libro.class);
			MongoCollection<Editorial> collection2 = database.getCollection("libros", Editorial.class);

			// Imprimir los datos de cada libro
			FindIterable<Libro> libros = collection.find();
			// FindIterable<Editorial> editoriales = collection2.find();
			oLibro = FXCollections.observableArrayList();
			for (Libro libro : libros) {
				oLibro.add(libro);
			}
			tabla.setItems(oLibro);

		} catch (MongoException e) {
			// handle MongoDB exception
			e.printStackTrace();
			throw e;
		} finally {
			if (null != mongoClient) {
				mongoClient.close();
			}
		}
	}
}
