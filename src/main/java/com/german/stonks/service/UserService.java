package com.german.stonks.service;

import com.german.stonks.repository.entity.User;

public interface UserService {
    User autenticarUsuario(String email, String senha);
    User cadastrarUsuario(User user);
    boolean validaUsuario(String email);
    User buscarPorId(Long id);
}
