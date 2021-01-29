package application;

public class Editorial {
	private String nombre;
	private String fecha_publicacion;


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFecha_publicacion() {
		return fecha_publicacion;
	}

	public void setFecha_publicacion(String fecha_publicacion) {
		this.fecha_publicacion = fecha_publicacion;
	}

	@Override
	public String toString() {
		return "Editorial [nombre=" + nombre + ", fecha_publicacion=" + fecha_publicacion + "]";
	}

}