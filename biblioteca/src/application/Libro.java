package application;

import org.bson.types.ObjectId;


public class Libro {
	ObjectId id;
	String isbn;
	String titulo;
	String descripcion;
	String autor;
	Editorial editorial;
	
	
	public Libro(String isbn, String titulo, String descripcion, String autor, Editorial editorial) {
		this.isbn = isbn;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.autor = autor;
		this.editorial = editorial;
	}
	/**
	 * @return the _id
	 */
	public ObjectId getId() {
		return id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void setId(ObjectId id) {
		this.id = id;
	}
	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}
	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the autor
	 */
	public String getAutor() {
		return autor;
	}
	/**
	 * @param autor the autor to set
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}
	/**
	 * @return the editorial
	 */
	public Editorial getEditorial() {
		return editorial;
	}
	/**
	 * @param editorial the editorial to set
	 */
	public void setEditorial(Editorial editorial) {
		this.editorial = editorial;
	}
	@Override
	public String toString() {
		return "Libro [isbn=" + isbn + ", titulo=" + titulo + ", descripcion=" + descripcion + ", autor=" + autor
				+ ", editorial=" + editorial + "]";
	}
	
	
}

