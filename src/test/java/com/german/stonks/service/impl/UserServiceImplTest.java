package com.german.stonks.service.impl;

import com.german.stonks.repository.UserRepositoryTest;
import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.UserRepository;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void deveAutenticarUmUsuario(){
        User user = UserRepositoryTest.createUser();
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User autenticado = userService.autenticarUsuario(user.getEmail(), user.getSenha());
        Assertions.assertTrue(autenticado == user);
    }

    @Test
    public void deveLançarRegraDeNegocioExceptionQuandoNaoEncontrarUsuario(){
        User user = UserRepositoryTest.createUser();
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        try {
            User autenticado = userService.autenticarUsuario(user.getEmail(), user.getSenha());
        }catch (RegraNegocioException e) {
            Assertions.assertEquals(e.getMessage(), "Usuário não encontrado.");
        }
    }

    @Test
    public void deveLançarRegraDeNegocioExceptionQuandoNaoSenhaEstiverIncorreta(){
        User user = UserRepositoryTest.createUser();
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        user.setSenha("321");
        try {
            User autenticado = userService.autenticarUsuario(user.getEmail(), user.getSenha());
        }catch (RegraNegocioException e) {
            Assertions.assertEquals(e.getMessage(), "Senha incorreta.");
        }
    }

    @Test
    public void deveValidarUmUsuario(){
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        boolean existe = userService.validaUsuario("email");
        System.out.println(existe);
        Assertions.assertTrue(existe);
    }

    @Test
    public void deveLancarRegraDeNegocioExceptionAoValidarEmailJaExistente(){
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        try {
            boolean existe = userService.validaUsuario("email");
        }catch(RegraNegocioException e){
            Assertions.assertEquals(e.getMessage(),"Já existe usuário cadastrado com este email.");
        }
    }

    @Test
    public void deveBuscarUmUsuarioPorId(){
        User user = UserRepositoryTest.createUser();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        User userSalvo = userService.buscarPorId(1L);
        Assertions.assertEquals(user.toString(), userSalvo.toString());
    }

    @Test
    public void deveDarRegraDeNegocioExceptionAoBuscarUsuarioPorIdENaoEncontrar(){
        User user = UserRepositoryTest.createUser();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            User userSalvo = userService.buscarPorId(1L);
        }catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Usuário não encontrado.");
        }
    }
}
