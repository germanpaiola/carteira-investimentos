package com.german.stonks.controller;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.PapelRepositoryTest;
import com.german.stonks.repository.UserRepositoryTest;
import com.german.stonks.repository.dto.PapelDto;
import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.PapelService;
import com.german.stonks.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PapelController.class)
public class PapelControllerTest {

    static final String API = "/api/papel";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @MockBean
    PapelService papelService;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    @Test
    public void deveRetornarOkAoCOmprarPapel() throws Exception {
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);
        PapelDto dto = createPapelDto();
        String json = new ObjectMapper().writeValueAsString(dto);
        Mockito.when(papelService.comprarPapel(user, "papel1", 10, 10)).thenReturn(papel);
        Mockito.when(userService.buscarPorId(Mockito.anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/comprar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(papel.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(papel.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("quantidade").value(papel.getQuantidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("valor").value(papel.getValor()));
    }

    public PapelDto createPapelDto(){
        PapelDto dto = PapelDto.builder()
                .nome("papel")
                .quantidade(10)
                .valor(100)
                .user(0l)
                .build();
        return dto;
    }

    @Test
    public void deveRetornarOkAoVenderPapel() throws Exception{
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);
        PapelDto dto = createPapelDto();
        String json = new ObjectMapper().writeValueAsString(dto);
        Mockito.when(papelService.venderPapel(papel)).thenReturn(papel);
        Mockito.when(userService.buscarPorId(Mockito.anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/" + papel.getId()))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveRetornarBadRequestAoReceberRegraDeNegocioException() throws Exception{
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);
        PapelDto dto = createPapelDto();
        String json = new ObjectMapper().writeValueAsString(dto);
        Mockito.when(papelService.venderPapel(Mockito.any())).thenThrow(RegraNegocioException.class);
        Mockito.when(userService.buscarPorId(Mockito.anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API.concat("/" + papel.getId()))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveRetornarListaDePapeisDeUmUsuario() throws Exception{
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);
        List<Papel> listaPapel = new ArrayList<>();
        listaPapel.add(papel);
        String json = new ObjectMapper().writeValueAsString(listaPapel);

        Mockito.when(papelService.verPapeis(Mockito.anyLong())).thenReturn(listaPapel);
        Mockito.when(userService.buscarPorId(Mockito.anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API.concat("/" + papel.getId()));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(json));
    }
}
