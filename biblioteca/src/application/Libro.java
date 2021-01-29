package application;

import org.bson.types.ObjectId;

public class Libro {
	private ObjectId id;
	private String isbn;
	private String titulo;
	private String descripcion;
	private String autor;
	private Editorial editorial;



	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Editorial getEditorial() {
		return editorial;
	}

	public void setEditorial(Editorial editorial) {
		this.editorial = editorial;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", isbn=" + isbn + ", titulo=" + titulo + ", descripcion=" + descripcion + ", autor="
				+ autor + ", editorial=" + editorial + "]";
	}

}
