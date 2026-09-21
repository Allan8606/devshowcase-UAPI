package com.devshowcase.controller;


import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/profiles")
    public ResponseEntity<ProfileResponseDTO> cadastrar(@RequestBody @Valid ProfileRequestDTO request){
        ProfileResponseDTO cadastrar = profileService.cadastrar(request);
        return ResponseEntity.ok(cadastrar);
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileResponseDTO>> listarTodos(){
        List<ProfileResponseDTO> profileResponseDTOS = profileService.listarTodos();
        return ResponseEntity.ok(profileResponseDTOS);
    }

     @GetMapping("/profiles/{id}")
    public ResponseEntity<ProfileResponseDTO> buscarPorId(@PathVariable Long id){
         ProfileResponseDTO profileResponseDTO = profileService.buscarPorId(id);
         return ResponseEntity.ok(profileResponseDTO);
     }

    @PutMapping("/profiles/{id}")
    public ResponseEntity<ProfileResponseDTO> editar(@PathVariable Long id, @RequestBody @Valid ProfileRequestDTO request){
        ProfileResponseDTO editar = profileService.editar(id, request);
        return ResponseEntity.ok(editar);
    }

    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        profileService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
