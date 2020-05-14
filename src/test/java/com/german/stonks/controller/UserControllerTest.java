package com.german.stonks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.UserRepositoryTest;
import com.german.stonks.repository.dto.UserDto;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    static final String API = "/api/user";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    @Test
    public void deveRetornarOkAoSalvarUsuario() throws Exception {
        User user = UserRepositoryTest.createUser();
        UserDto dto = createUserDto();
        Mockito.when(userService.cadastrarUsuario(user)).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
                                                                .accept(JSON)
                                                                .contentType(JSON)
                                                                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(user.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("senha").value(user.getSenha()))
                .andExpect(MockMvcResultMatchers.jsonPath("saldo").value(user.getSaldo()));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarUsuarioJaExistente() throws JsonProcessingException {
        User user = UserRepositoryTest.createUser();
        UserDto dto = createUserDto();
        Mockito.when(userService.cadastrarUsuario(Mockito.any())).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        try {
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void deveAutenticarUsuario() throws Exception {
        User user = UserRepositoryTest.createUser();
        UserDto dto = createUserDto();
        Mockito.when(userService.autenticarUsuario(user.getEmail(), user.getSenha())).thenReturn(user);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("senha").value(user.getSenha()));
    }

    @Test
    public void deveRetornarBadRequestAoAutenticarUsuarioIncorretamente() throws Exception {
        User user = UserRepositoryTest.createUser();
        UserDto dto = createUserDto();
        dto.setSenha("321");
        Mockito.when(userService.autenticarUsuario(Mockito.anyString(), Mockito.anyString())).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    public UserDto createUserDto(){
        UserDto dto = UserDto.builder()
                .nome("user")
                .email("user@email.com")
                .senha("123")
                .build();
        return dto;
    }

}
