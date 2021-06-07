package org.springframework.Ifiweb.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
