package com.german.stonks.controller;

import com.german.stonks.exceptions.RegraNegocioException;
import com.german.stonks.repository.dto.UserDto;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody UserDto dto){
        User user = User.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        try{
            userService.cadastrarUsuario(user);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticarUsuario(@RequestBody UserDto dto){
        User user = User.builder()
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        try{
            userService.autenticarUsuario(dto.getEmail(), dto.getSenha());
        }catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(user);
    }
}
