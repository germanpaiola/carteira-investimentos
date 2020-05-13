package com.german.stonks.repository.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "papel", schema = "stonks")
@ToString
public class Papel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "valor" )
    private double valor;


    @Column(name = "quantidade")
    private int quantidade;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
