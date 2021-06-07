package org.springframework.Editrans.repository;

import org.springframework.Editrans.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Buscar usuario por nombre
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
