package com.german.stonks.repository;

import com.german.stonks.repository.UserRepository;
import com.german.stonks.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void deveVerificarAExistenciaDeUmEmail() {
        userRepository.deleteAll();
        User user = createUser();
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail("user@email.com");
        Assertions.assertTrue(exists);
    }

    @Test
    public void deveRetornarOptionalVazioAoNaoEncontrarUsuario(){
        userRepository.deleteAll();
        Optional<User> user = userRepository.findByEmail("user@email.com");
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    public void deveRetornarUsuarioCadastradoComEmailInformado(){
        userRepository.deleteAll();
        User user = createUser();
        userRepository.save(user);
        Optional<User> userSalvo = userRepository.findByEmail("user@email.com");
        Assertions.assertTrue(user.getEmail().equals(userSalvo.get().getEmail())
                                    && user.getNome().equals(userSalvo.get().getNome())
                                    && user.getId() == userSalvo.get().getId()
                                    && user.getSaldo() == userSalvo.get().getSaldo()
                                    && user.getSenha().equals(userSalvo.get().getSenha()));
    }

    public static User createUser(){
        User user = User.builder()
                .nome("user")
                .email("user@email.com")
                .senha("123")
                .build();
        return user;
    }
}
