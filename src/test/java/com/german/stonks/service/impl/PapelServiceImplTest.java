package com.german.stonks.service.impl;

import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.PapelRepository;
import com.german.stonks.repository.PapelRepositoryTest;
import com.german.stonks.repository.UserRepository;
import com.german.stonks.repository.UserRepositoryTest;
import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.PapelService;
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
public class PapelServiceImplTest {

    @Autowired
    PapelService papelService;

    @MockBean
    PapelRepository papelRepository;

    @Test
    public void deveSalvarUmNovoPapel(){
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);

        Mockito.when(papelRepository.findByNomeAndUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.empty());
        Mockito.when(papelRepository.save(Mockito.any())).thenReturn(papel);
        Papel papelSalvo = papelService.comprarPapel(user, papel.getNome(), papel.getValor(), papel.getQuantidade());

        Assertions.assertTrue(papelSalvo.toString().equals(papel.toString()));
    }

    @Test
    public void deveAtualizarUmPapelExistente(){
        User user = UserRepositoryTest.createUser();
        Papel papelSalvo = PapelRepositoryTest.createPapel();
        papelSalvo.setUser(user);

        Papel papelNovo = PapelRepositoryTest.createPapel();
        papelNovo.setQuantidade(10);
        papelNovo.setValor(200);
        papelNovo.setUser(user);

        Papel papelAtualizado = PapelRepositoryTest.createPapel();
        papelAtualizado.setValor(150);
        papelAtualizado.setQuantidade(20);
        papelAtualizado.setUser(user);

        Mockito.when(papelRepository.findByNomeAndUser(Mockito.anyString(), Mockito.anyLong())).thenReturn(Optional.of(papelSalvo));
        Mockito.doNothing().when(papelRepository).updatePapel(papelAtualizado.getQuantidade(),
                                                            papelAtualizado.getValor(),
                                                            papelAtualizado.getNome(),
                                                            papelAtualizado.getUser().getId());
        Papel papel = papelService.comprarPapel(user, papelNovo.getNome(), papelNovo.getValor(), papelNovo.getQuantidade());

        System.out.println(papel.toString());
        System.out.println(papelAtualizado.toString());
        Assertions.assertTrue(papel.toString().equals(papelAtualizado.toString()));
    }

    @Test
    public void deveLancarRegradeNegocioExceptionSeQuantidadeVendidaForMenorQueZero(){
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);

        Papel papelSalvo = PapelRepositoryTest.createPapel();
        papelSalvo.setQuantidade(5);
        papelSalvo.setUser(user);

        Mockito.when(papelRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(papelSalvo));
        try{
            papelService.venderPapel(papel);
        }catch(RegraNegocioException e){
            Assertions.assertEquals(e.getMessage(), "Quantidade inválida.");
        }
    }

    @Test
    public void deveLancarRegraDeNegocioExceptionSeVenderPapelInexistente(){
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);

        Papel papelSalvo = PapelRepositoryTest.createPapel();
        papelSalvo.setQuantidade(15);
        papelSalvo.setUser(user);

        Mockito.when(papelRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try{
            papelService.venderPapel(papel);
        }catch(RegraNegocioException e){
            Assertions.assertEquals(e.getMessage(), "Papel inválido.");
        }
    }

    @Test
    public void deveDiminuirAQuantidadeDeUmPapelSalvoEAtualizar(){
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);

        Papel papelSalvo = PapelRepositoryTest.createPapel();
        papelSalvo.setQuantidade(15);
        papelSalvo.setUser(user);

        Mockito.when(papelRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(papelSalvo));
        boolean vendido = papelService.venderPapel(papel);

        Mockito.verify(papelRepository, Mockito.times(1)).updatePapel(Mockito.anyInt(),
                                                                            Mockito.anyDouble(),
                                                                            Mockito.anyString(),
                                                                            Mockito.anyLong());
        Assertions.assertTrue(vendido);
    }

    @Test
    public void deveDiminuirAQuantidadeDeUmPapelEDeletar(){
        User user = UserRepositoryTest.createUser();
        Papel papel = PapelRepositoryTest.createPapel();
        papel.setUser(user);

        Papel papelSalvo = PapelRepositoryTest.createPapel();
        papelSalvo.setUser(user);

        Mockito.when(papelRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(papelSalvo));
        boolean vendido = papelService.venderPapel(papel);

        Mockito.verify(papelRepository, Mockito.times(1)).delete(papelSalvo);
        Assertions.assertTrue(vendido);
    }
}
