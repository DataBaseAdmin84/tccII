package com.controller;

import com.repository.ArquivoRepository;
import com.service.AulaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/material")
public class AulaArquivoController {
    private static final Logger log = LoggerFactory.getLogger(AulaArquivoController.class);

    @Autowired
    AulaService aulaService;

    @Autowired
    ArquivoRepository arquivoRepository;

    @PostMapping("/excluir/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            var arquivoOpt = arquivoRepository.findById(id);
            arquivoOpt.ifPresent(arquivo -> aulaService.removerVinculosPorArquivo(arquivo));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ocorreu um erro ao excluir o arquivo{}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}