package com.devshowcase.controller;

import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @PostMapping("/technologies")
    public ResponseEntity<TechnologyResponseDTO> cadastrar(@RequestBody TechnologyRequestDTO request){
        return ResponseEntity.ok(technologyService.cadastrar(request));
    }

    @GetMapping("/technologies")
    public ResponseEntity<List<TechnologyResponseDTO>> listarTodos(){
        return ResponseEntity.ok(technologyService.listarTodos());
    }

    @GetMapping("/technologies/{id}")
    public ResponseEntity<TechnologyResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(technologyService.buscarPorid(id));
    }

    @PutMapping("/technologies/{id}")
    public ResponseEntity<TechnologyResponseDTO> editar(@PathVariable Long id, @RequestBody TechnologyRequestDTO request){
        return ResponseEntity.ok(technologyService.editar(id, request));
    }

    @DeleteMapping("/technologies/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        technologyService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
