package org.springframework.Ifiweb.service;

import org.springframework.Ifiweb.model.User;

public interface UserService {

	void save(User user);
    User findByUsername(String username);
}
