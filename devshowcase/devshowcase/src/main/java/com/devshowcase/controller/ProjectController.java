package com.devshowcase.controller;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> cadastrar(@RequestBody ProjectRequestDTO request){
        return ResponseEntity.ok(projectService.cadastrar(request));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponseDTO>> listarTodos(){
        return ResponseEntity.ok(projectService.listarTodos());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(projectService.buscarPorId(id));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDTO> editar(@PathVariable Long id, @RequestBody ProjectRequestDTO request){
        return ResponseEntity.ok(projectService.editar(id, request));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        projectService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
