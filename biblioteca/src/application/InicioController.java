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
import org.bson.types.ObjectId;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
	@FXML
	private Button btnCrear;
	private ObservableList<Libro> oLibro;

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

			Document docEditorial = new Document()
					.append("nombre", nombreEditorial)
					.append("fecha_publicacion", fecha);
			Document docLibro = new Document()
					.append("isbn", isbn)
					.append("titulo", titulo)
					.append("descripcion", descripcion)
					.append("autor", autor)
					.append("editorial", docEditorial);

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
	public void eliminarLibro() throws Exception {
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

			Libro libro = this.tabla.getSelectionModel().getSelectedItem();
			String titulo = txtTitulo.getText();
			String descripcion = txtDescripcion.getText();
			String autor = txtAutor.getText();
			String nombreEditorial = txtEditorial.getText();
			String fecha = txtPublicacion.getText();
			String isbn = txtIsbn.getText();

			db.getCollection("libros").updateOne(new Document("_id", libro.getId()),
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

	/**
	 * Refresca la tabla cargando los datos
	 * 
	 * @param ev
	 */
	@FXML
	public void refresh(ActionEvent ev) {
		cargarTabla();
	}

	/**
	 * Inicio de la aplicacion
	 */
	@FXML
	public void initialize() {
		cargarTabla();
		comboBox.getItems().addAll("ISBN", "AUTOR", "EDITORIAL", "FECHA DE PUBLICACION", "TITULO");
	}

	/**
	 * Carga la tabla de la bbdd
	 */
	public void cargarTabla() throws MongoException {
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

	/**
	 * Selecciona el libro de la tabla
	 * 
	 * @param ev
	 */
	@FXML
	public void seleccionaTabla(MouseEvent ev) {
		Libro libro = this.tabla.getSelectionModel().getSelectedItem();

		if (libro != null) {
			this.txtAutor.setText(libro.getAutor());
			this.txtDescripcion.setText(libro.getDescripcion());
			this.txtIsbn.setText(libro.getIsbn());
			this.txtTitulo.setText(libro.getTitulo());
			this.txtEditorial.setText(libro.getEditorial().getNombre());
			this.txtPublicacion.setText(libro.getEditorial().getFecha_publicacion());
		}
	}

	/**
	 * Filtra
	 * 
	 * @param ev
	 * @throws MongoException
	 */
	@FXML
	public void filtrar(ActionEvent ev) throws MongoException {
			String valorComboBox = comboBox.getValue();
			MongoClient mongoClient = null;
			ObservableList<Libro> lLibro = FXCollections.observableArrayList();
			try {
				mongoClient = MongoClients.create();
				MongoDatabase database = mongoClient.getDatabase("biblioteca");

				if (valorComboBox == "ISBN") {
					metodoFiltrar(database, lLibro, "isbn");
				} else if (valorComboBox == "AUTOR") {
					metodoFiltrar(database, lLibro, "autor");
				} else if (valorComboBox == "EDITORIAL") {
					metodoFiltrar(database, lLibro, "editorial.nombre");
				} else if (valorComboBox == "FECHA DE PUBLICACION") {
					metodoFiltrar(database, lLibro, "editorial.fecha_publicacion");
				} else if (valorComboBox == "TITULO") {
					metodoFiltrar(database, lLibro, "titulo");
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

	/**
	 * Filtra por el parametro pasado
	 * 
	 * @param database
	 * @param lLibro
	 * @param tipoFiltro
	 */
	public void metodoFiltrar(MongoDatabase database, ObservableList<Libro> lLibro, String tipoFiltro) {
		Document doc = new Document(tipoFiltro, txtBuscar.getText());
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
}
