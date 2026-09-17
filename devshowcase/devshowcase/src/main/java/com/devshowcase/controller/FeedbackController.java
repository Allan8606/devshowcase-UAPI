package com.devshowcase.controller;


import com.devshowcase.dto.request.FeedbackRequestDTO;
import com.devshowcase.dto.request.TechnologyRequestDTO;
import com.devshowcase.dto.response.FeedbackResponseDTO;
import com.devshowcase.dto.response.TechnologyResponseDTO;
import com.devshowcase.service.FeedbackService;
import com.devshowcase.service.TechnologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedbacks")
    public ResponseEntity<FeedbackResponseDTO> cadastrar(@RequestBody FeedbackRequestDTO request){
        return ResponseEntity.ok(feedbackService.cadastrar(request));
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackResponseDTO>> listarTodos(){
        return ResponseEntity.ok(feedbackService.listarTodos());
    }

    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackResponseDTO> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(feedbackService.buscarPorId(id));
    }

    @PutMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackResponseDTO> editar(@PathVariable Long id, @RequestBody FeedbackRequestDTO request){
        return ResponseEntity.ok(feedbackService.editar(id, request));
    }

    @DeleteMapping("/feedbacks/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        feedbackService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
