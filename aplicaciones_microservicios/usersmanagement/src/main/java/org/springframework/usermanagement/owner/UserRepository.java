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

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/*
 * Clase Repositorio para interactuar con usuario.
 */
public interface UserRepository extends Repository<User, Integer> {

	/*
	 * Devuelve el usuario buscado por código silcon
	 */
	@Query("SELECT DISTINCT user FROM User user WHERE user.silcon LIKE :silcon%")
	@Transactional(readOnly = true)
	Collection<User> findBySilcon(@Param("silcon") String silcon);

	/*
	 * Devuelve el usuario buscado por id
	 */
	@Query("SELECT user FROM User user WHERE user.id =:id")
	@Transactional(readOnly = true)
	User findById(@Param("id") Integer id);

	/*
	 * Borra el usuario indicado
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM User user WHERE user.id =:id")
	void deleteById(@Param("id") Integer id);

	/*
	 * Guarda los datos del usuario
	 */
	void save(User user);

}
