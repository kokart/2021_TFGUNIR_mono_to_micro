package org.springframework.Editrans.service;

import org.springframework.Editrans.model.User;

public interface UserService {
    void save(User user);


    User findByUsername(String username);
}
