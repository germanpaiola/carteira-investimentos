package com.german.stonks.service;

import com.german.stonks.repository.PapelRepository;
import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;

import java.util.List;

public interface PapelService {
    Papel comprarPapel(User user, String nome, double valor, int quantidade);
    Papel venderPapel(Papel papel);
    List<Papel> verPapeis(long id);
    Papel buscarPorId(Long id);
}
