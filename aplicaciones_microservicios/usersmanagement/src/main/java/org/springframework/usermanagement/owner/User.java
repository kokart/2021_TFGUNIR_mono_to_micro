/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.usermanagement.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.usermanagement.model.Person;

/*
 * Clase representar al usuario
 */

@Entity
@Table(name = "users")
public class User extends Person {

	@Column(name = "centro")
	@NotEmpty
	private String centro;

	@Column(name = "silcon")
	@NotEmpty
	private String silcon;

	@Column(name = "aplicacion")
	@NotEmpty
	private String aplicacion;

	public String getCentro() {
		return this.centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public String getSilcon() {
		return this.silcon;
	}

	public void setSilcon(String silcon) {
		this.silcon = silcon;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("new", this.isNew()).append("apellido", this.getApellido())
				.append("nombre", this.getNombre()).append("centro", this.centro).append("silcon", this.silcon)
				.append("aplicacion", this.silcon).toString();
	}

}
