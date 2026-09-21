package com.devshowcase.controller;

import com.devshowcase.dto.request.ProfileRequestDTO;
import com.devshowcase.dto.request.ProjectRequestDTO;
import com.devshowcase.dto.response.ProfileResponseDTO;
import com.devshowcase.dto.response.ProjectResponseDTO;
import com.devshowcase.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ProjectResponseDTO> cadastrar(@RequestBody @Valid ProjectRequestDTO request){
        return ResponseEntity.ok(projectService.cadastrar(request));
    }

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectResponseDTO>> buscarProjetos(
            @RequestParam(required = false) String technology,
            Pageable pageable) {

        return ResponseEntity.ok(
                projectService.buscarComFiltro(technology, pageable)
        );
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(projectService.buscarPorId(id));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectResponseDTO> editar(@PathVariable Long id, @RequestBody @Valid ProjectRequestDTO request){
        return ResponseEntity.ok(projectService.editar(id, request));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        projectService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/projects/{id}/upvote")
    public ResponseEntity<ProjectResponseDTO> adicionarUpvote(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.adicionarUpvote(id));
    }



}
