package com.german.stonks.service.impl;


import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.UserRepository;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User autenticarUsuario(String email, String senha) {
        Optional<User> userSalvo = userRepository.findByEmail(email);
        if(userSalvo.isEmpty())
            throw new RegraNegocioException("Usuário não encontrado.");
        if(!userSalvo.get().getSenha().equals(senha))
            throw new RegraNegocioException("Senha incorreta.");
        return userSalvo.get();
    }

    @Override
    public User cadastrarUsuario(User user) {
        validaUsuario(user.getEmail());
        user = userRepository.save(user);
        return user;
    }

    @Override
    public boolean validaUsuario(String email) {
        boolean existe = userRepository.existsByEmail(email);
        if(existe)
            throw new RegraNegocioException("Já existe usuário cadastrado com este email.");
        return true;
    }

    public User buscarPorId(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new RegraNegocioException("Usuário não encontrado.");
        return optionalUser.get();
    }
}