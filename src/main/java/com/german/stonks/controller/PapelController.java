package com.german.stonks.controller;

import com.german.stonks.repository.dto.PapelDto;
import com.german.stonks.repository.entity.Papel;
import com.german.stonks.repository.entity.User;
import com.german.stonks.service.PapelService;
import com.german.stonks.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papel")
public class PapelController {

    private PapelService papelService;
    private UserService userService;

    public PapelController(PapelService papelService, UserService userService){
        this.papelService = papelService;
        this.userService =  userService;
    }

    @PostMapping("/comprar")
    public ResponseEntity comprarPapel(@RequestBody PapelDto dto){
        Papel papel = Papel.builder()
                .nome(dto.getNome())
                .quantidade(dto.getQuantidade())
                .valor(dto.getValor())
                .user(userService.buscarPorId(dto.getUser()))
                .build();
        try{
            papelService.comprarPapel(papel.getUser(), dto.getNome(), dto.getValor(), dto.getQuantidade());
            return ResponseEntity.ok(papel);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity venderPapel(@PathVariable("id") Long id){
        Papel papel = papelService.buscarPorId(id);
        try{
            papelService.venderPapel(papel);
            return ResponseEntity.ok(papel);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity verPapeis(@PathVariable Long id){
        List<Papel> papelList = papelService.verPapeis(id);

        return ResponseEntity.ok(papelList);
    }
}
