package com.german.stonks.repository.dto;

import com.german.stonks.repository.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PapelDto {
    private String nome;
    private int quantidade;
    private double valor;
    private Long user;
}
