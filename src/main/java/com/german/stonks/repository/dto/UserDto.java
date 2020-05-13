package com.german.stonks.repository.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String email;
    private String nome;
    private String senha;
}
