package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.util.Iterator;
import java.util.List;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
	@FXML
	private TextField txtIsbn;
	@FXML
	private TextField txtTitulo;
	@FXML
	private TextField txtAutor;
	@FXML
	private TextArea txtDescripcion;
	@FXML
	private TextField txtEditorial;
	@FXML
	private TextField txtPublicacion;

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private TextField txtBuscar;

	private ObservableList<Libro> oLibro;

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

	/**
	 * Crea libro
	 * 
	 * @param isbn
	 * @param titulo
	 * @param descripcion
	 * @param autor
	 * @param nombreEditorial
	 * @param fecha
	 */
	public void crearLibro(ActionEvent ev) {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");

			String isbn = txtIsbn.getText();
			String titulo = txtTitulo.getText();
			String descripcion = txtDescripcion.getText();
			String autor = txtAutor.getText();
			String nombreEditorial = txtEditorial.getText();
			String fecha = txtDescripcion.getText();

			Document docEditorial = new Document().append("nombre", nombreEditorial).append("fecha_publicacion", fecha);
			Document docLibro = new Document().append("isbn", isbn).append("titulo", titulo)
					.append("descripcion", descripcion).append("autor", autor).append("editorial", docEditorial);

			db.getCollection("libros").insertOne(docLibro);
			cargarTabla();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	/**
	 * Elimina un libro
	 * 
	 * @param isbn
	 */
	public void eliminarLibro() {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");

			CodecRegistry pojoCodec = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
					CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
			db = db.withCodecRegistry(pojoCodec);
			MongoCollection<Libro> collection = db.getCollection("libros", Libro.class);

			String isbn = tabla.getSelectionModel().getSelectedItem().getIsbn();

			collection.deleteOne(new Document("isbn", isbn));

			cargarTabla();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	/**
	 * Actualiza la bbdd
	 */
	public void actualizarLibro() {
		MongoClient mongo = null;
		try {
			mongo = MongoClients.create();
			MongoDatabase db = mongo.getDatabase("biblioteca");

			String titulo = txtTitulo.getText();
			String descripcion = txtDescripcion.getText();
			String autor = txtAutor.getText();
			String nombreEditorial = txtEditorial.getText();
			String fecha = txtPublicacion.getText();

			db.getCollection("libros").updateOne(new Document(),
					new Document("$set",
							new Document("editorial.nombre", nombreEditorial).append("titulo", titulo)
									.append("autor", autor).append("descripcion", descripcion)
									.append("editorial.fecha_publicacion", fecha)));
			cargarTabla();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongo != null) {
				mongo.close();
			}
		}
	}

	@FXML
	public void refresh(ActionEvent ev) {
		cargarTabla();
	}

	@FXML
	public void initialize() {
		cargarTabla();
		comboBox.getItems().addAll("ISBN", "AUTOR", "EDITORIAL", "FECHA DE PUBLICACION", "TITULO");
	}

	/**
	 * Carga la tabla de la bbdd
	 */
	public void cargarTabla() {
		this.isbn.setCellValueFactory(new PropertyValueFactory("isbn"));
		this.titulo.setCellValueFactory(new PropertyValueFactory("titulo"));
		this.autor.setCellValueFactory(new PropertyValueFactory("autor"));
		this.fechaPublicacion.setCellValueFactory(new PropertyValueFactory("editorial"));
		this.descripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));

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

	public void seleccionaTabla(MouseEvent ev) {
		Libro libro = this.tabla.getSelectionModel().getSelectedItem();

		if (libro != null) {
			this.txtAutor.setText(libro.getAutor());
			this.txtDescripcion.setText(libro.getDescripcion());
			this.txtIsbn.setText(libro.getIsbn());
			this.txtTitulo.setText(libro.getTitulo());
		}
	}

	/**
	 * Filtra
	 * @param ev
	 * @throws MongoException
	 */
	public void filtrar(ActionEvent ev) throws MongoException{
		String valorComboBox = comboBox.getValue();
		MongoClient mongoClient = null;
		ObservableList<Libro> lLibro = FXCollections.observableArrayList();
		try {
			mongoClient = MongoClients.create();
			MongoDatabase database = mongoClient.getDatabase("biblioteca");

			if (valorComboBox == "ISBN") {
				Document doc = new Document("isbn", txtBuscar.getText());
				FindIterable find = database.getCollection("libros").find(doc);

				Libro libro;
				Editorial editorial;
				Iterator<Document> iterator = find.iterator();

				while (iterator.hasNext()) {
					doc = iterator.next();
					libro = new Libro();
					editorial = new Editorial();
					libro.setIsbn(doc.getString("isbn"));
					libro.setTitulo(doc.getString("titulo"));
					libro.setAutor(doc.getString("autor"));
					libro.setDescripcion(doc.getString("descripcion"));
					editorial.setNombre(doc.getString("nombre"));
					editorial.setFecha_publicacion(doc.getString("fecha_publicacion"));
					lLibro.add(libro);
					tabla.setItems(lLibro);
				}
			} else if (valorComboBox == "AUTOR") {
				Document doc = new Document("autor", txtBuscar.getText());
				FindIterable find = database.getCollection("libros").find(doc);

				Libro libro;
				Editorial editorial;
				Iterator<Document> iterator = find.iterator();

				while (iterator.hasNext()) {
					doc = iterator.next();
					libro = new Libro();
					editorial = new Editorial();
					libro.setIsbn(doc.getString("isbn"));
					libro.setTitulo(doc.getString("titulo"));
					libro.setAutor(doc.getString("autor"));
					libro.setDescripcion(doc.getString("descripcion"));
					editorial.setNombre(doc.getString("nombre"));
					editorial.setFecha_publicacion(doc.getString("fecha_publicacion"));
					lLibro.add(libro);
					tabla.setItems(lLibro);
				}
			} else if (valorComboBox == "EDITORIAL") {
				Document doc = new Document("editorial.nombre", txtBuscar.getText());
				FindIterable find = database.getCollection("libros").find(doc);

				Libro libro;
				Editorial editorial;
				Iterator<Document> iterator = find.iterator();

				while (iterator.hasNext()) {
					doc = iterator.next();
					libro = new Libro();
					editorial = new Editorial();
					libro.setIsbn(doc.getString("isbn"));
					libro.setTitulo(doc.getString("titulo"));
					libro.setAutor(doc.getString("autor"));
					libro.setDescripcion(doc.getString("descripcion"));
					editorial.setNombre(doc.getString("nombre"));
					editorial.setFecha_publicacion(doc.getString("fecha_publicacion"));
					lLibro.add(libro);
					tabla.setItems(lLibro);
				}
			} else if (valorComboBox == "FECHA DE PUBLICACION") {
				Document doc = new Document("editorial.fecha_publicacion", txtBuscar.getText());
				FindIterable find = database.getCollection("libros").find(doc);

				Libro libro;
				Editorial editorial;
				Iterator<Document> iterator = find.iterator();

				while (iterator.hasNext()) {
					doc = iterator.next();
					libro = new Libro();
					editorial = new Editorial();
					libro.setIsbn(doc.getString("isbn"));
					libro.setTitulo(doc.getString("titulo"));
					libro.setAutor(doc.getString("autor"));
					libro.setDescripcion(doc.getString("descripcion"));
					editorial.setNombre(doc.getString("nombre"));
					editorial.setFecha_publicacion(doc.getString("fecha_publicacion"));
					lLibro.add(libro);
					tabla.setItems(lLibro);
				}
			} else if (valorComboBox == "TITULO") {
				Document doc = new Document("titulo", txtBuscar.getText());
				FindIterable find = database.getCollection("libros").find(doc);

				Libro libro;
				Editorial editorial;
				Iterator<Document> iterator = find.iterator();

				while (iterator.hasNext()) {
					doc = iterator.next();
					libro = new Libro();
					editorial = new Editorial();
					libro.setIsbn(doc.getString("isbn"));
					libro.setTitulo(doc.getString("titulo"));
					libro.setAutor(doc.getString("autor"));
					libro.setDescripcion(doc.getString("descripcion"));
					editorial.setNombre(doc.getString("nombre"));
					editorial.setFecha_publicacion(doc.getString("fecha_publicacion"));
					lLibro.add(libro);
					tabla.setItems(lLibro);
				}
			}
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
