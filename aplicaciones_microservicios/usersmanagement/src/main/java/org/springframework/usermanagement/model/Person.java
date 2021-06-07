/*
 * Clase para el modelo Persona/usuario
 */
package org.springframework.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;


@MappedSuperclass
public class Person extends BaseEntity {

	@Column(name = "nombre")
	@NotEmpty
	private String nombre;

	@Column(name = "apellido")
	@NotEmpty
	private String apellido;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

}
