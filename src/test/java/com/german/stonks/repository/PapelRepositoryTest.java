package com.german.stonks.repository;

import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PapelRepositoryTest {

    @Autowired
    PapelRepository papelRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void deveRetornarPapelPorNomeEUsuario(){
        User user = UserRepositoryTest.createUser();
        userRepository.save(user);
        Papel papel = createPapel();
        papel.setUser(user);
        papelRepository.save(papel);

        Optional<Papel> papelSalvo = papelRepository.findByNomeAndUser("papel", user.getId());
        System.out.println(papelSalvo.get());
        Assertions.assertEquals(papelSalvo.get().toString(), papel.toString());
    }

    @Test
    public void deveRetornarPapelAtualizado(){
        User user = UserRepositoryTest.createUser();
        userRepository.save(user);
        Papel papel = createPapel();
        papel.setUser(user);
        papelRepository.save(papel);

        Papel novoPapel = createPapel();
        novoPapel.setUser(user);
        novoPapel.setQuantidade(20);
        novoPapel.setValor(300);

        papelRepository.updatePapel(novoPapel.getQuantidade(), novoPapel.getValor(), papel.getNome(), papel.getUser().getId());
        Optional<Papel> papelAtualizado = papelRepository.findByNomeAndUser(novoPapel.getNome(), novoPapel.getUser().getId());
        Assertions.assertTrue(papelAtualizado.get().getNome().equals(novoPapel.getNome())
                            && papelAtualizado.get().getQuantidade() == novoPapel.getQuantidade()
                            && papelAtualizado.get().getValor() == novoPapel.getValor()
                            && papelAtualizado.get().getUser().getId() == novoPapel.getUser().getId());
    }

    @Test
    public void deveRetornarListaDePapeisDeUmUsuario(){
        User user = UserRepositoryTest.createUser();
        userRepository.save(user);

        Papel papel1 = createPapel();
        papel1.setUser(user);
        papel1.setNome("papel1");
        papelRepository.save(papel1);

        Papel papel2 = createPapel();
        papel2.setUser(user);
        papel2.setNome("papel2");
        papelRepository.save(papel2);

        Papel papel3 = createPapel();
        papel3.setUser(user);
        papel3.setNome("papel3");
        papelRepository.save(papel3);

        List<Papel> papelList = new ArrayList<Papel>();
        papelList.add(papel1);
        papelList.add(papel2);
        papelList.add(papel3);
        System.out.println(papelList);
        List<Papel> papelListSalva = papelRepository.findAllByUserId(user.getId());
        System.out.println(papelListSalva);

        Assertions.assertEquals(papelList.toString(), papelListSalva.toString());
    }

    public static Papel createPapel(){
        Papel papel = Papel.builder()
                .nome("papel")
                .quantidade(10)
                .valor(100)
                .build();
        return papel;
    }

}
