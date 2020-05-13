package com.german.stonks.repository;

import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface PapelRepository extends JpaRepository <Papel, Long>{


    @Query(value = "SELECT * FROM stonks.papel WHERE papel.nome = :nome AND papel.user_id= :id", nativeQuery = true)
    Optional<Papel> findByNomeAndUser(@Param("nome") String nome, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Papel set quantidade = :quantidade, valor = :valor WHERE nome = :nome AND user_id = :user_id")
    void updatePapel(@Param("quantidade") int quantidade, @Param("valor") double valor, @Param("nome") String nome, @Param("user_id") long userId);

    @Query(value = "SELECT * FROM stonks.papel WHERE papel.user_id = :id", nativeQuery = true)
    List<Papel> findAllByUserId(Long id);
}
