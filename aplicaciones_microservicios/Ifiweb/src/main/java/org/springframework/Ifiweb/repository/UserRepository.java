package org.springframework.Ifiweb.repository;

import org.springframework.Ifiweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
