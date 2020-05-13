package com.german.stonks.service.impl;

import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.PapelRepository;
import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.PapelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PapelServiceImpl implements PapelService {

    private PapelRepository papelRepository;

    @Autowired
    public PapelServiceImpl(PapelRepository papelRepository){
        super();
        this.papelRepository = papelRepository;
    }

    @Override
    public Papel comprarPapel(User user, String nome, double valor, int quantidade) {
        Papel papel = Papel.builder()
                .user(user)
                .nome(nome)
                .valor(valor)
                .quantidade(quantidade)
                .build();
        Optional<Papel> papelSalvo = papelRepository.findByNomeAndUser(nome, user.getId());
        if(papelSalvo.isPresent()){
            int novaQuantidade = papelSalvo.get().getQuantidade() + quantidade;
            double novoValor = (((papelSalvo.get().getQuantidade() * papelSalvo.get().getValor())
                                + (papel.getValor() * papel.getQuantidade()))
                                / novaQuantidade);
            papel.setQuantidade(novaQuantidade);
            papel.setValor(novoValor);

            papelRepository.updatePapel(papel.getQuantidade(), papel.getValor(), papel.getNome(), papel.getUser().getId());
        }else{
            papelRepository.save(papel);
        }
        return papel;
    }

    @Override
    public Papel venderPapel(Papel papel) {
        Optional<Papel> papelSalvo = papelRepository.findById(papel.getId());
        if(papelSalvo.isPresent()){
            int novaQuantidade = papelSalvo.get().getQuantidade() - papel.getQuantidade();
            if(novaQuantidade > 0){
                papel.setQuantidade(novaQuantidade);
                papelRepository.updatePapel(papel.getQuantidade(), papel.getValor(), papel.getNome(), papel.getUser().getId());
            }else if(novaQuantidade == 0){
                papelRepository.delete(papelSalvo.get());
            }else{
                throw new RegraNegocioException("Quantidade inválida.");
            }
            return papel;
        }else{
            throw new RegraNegocioException("Papel inválido.");
        }
    }

    @Override
    public List<Papel> verPapeis(long id) {
        List<Papel> papelList = papelRepository.findAllByUserId(id);
        return papelList;
    }

    @Override
    public Papel buscarPorId(Long id) {
        Optional<Papel> optionalPapel = papelRepository.findById(id);
        if(optionalPapel.isEmpty())
            throw new RegraNegocioException("Papel não encontrado");
        return optionalPapel.get();
    }
}
