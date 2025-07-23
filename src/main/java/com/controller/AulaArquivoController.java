package com.controller;

import com.model.Arquivo;
import com.repository.ArquivoRepository;
import com.service.AulaService;
import com.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/material")
public class AulaArquivoController {
    private static final Logger log = LoggerFactory.getLogger(AulaArquivoController.class);

    private final AulaService aulaService;
    private final ArquivoRepository arquivoRepository;
    private final S3Service s3Service;

    @Autowired
    public AulaArquivoController(AulaService aulaService, ArquivoRepository arquivoRepository, S3Service s3Service) {
        this.aulaService = aulaService;
        this.arquivoRepository = arquivoRepository;
        this.s3Service = s3Service;
    }

    @GetMapping("/visualizar/{id}")
    public ModelAndView visualizar(@PathVariable Long id) {
        var arquivoOpt = arquivoRepository.findById(id);

        if (arquivoOpt.isEmpty()) {
            log.warn("Tentativa de visualizar arquivo com id não existente: {}", id);
            return new ModelAndView("error/404");
        }

        Arquivo arquivo = arquivoOpt.get();

        try {
            String presignedUrl = s3Service.gerarUrlPresignadaParaVisualizacao(arquivo.getCaminho(), arquivo.getNome());

            ModelAndView mv = new ModelAndView("material/visualizador"); // Nome do arquivo HTML
            mv.addObject("arquivo", arquivo);
            mv.addObject("urlVisualizacao", presignedUrl);
            return mv;

        } catch (RuntimeException e) {
            log.error("Erro ao tentar gerar a URL pré-assinada para a chave {}: {}", arquivo.getCaminho(), e.getMessage());
            // Retorna para uma página de erro genérica
            return new ModelAndView("error/500");
        }
    }
    @PostMapping("/excluir/{id}")
    @Transactional // Garante que todas as operações (banco e S3) sejam um sucesso ou falhem juntas
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        log.info("Iniciando processo de exclusão para o arquivo de id: {}", id);

        var arquivoOpt = arquivoRepository.findById(id);
        if (arquivoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Arquivo arquivo = arquivoOpt.get();

        try {
            aulaService.removerVinculosPorArquivo(arquivo);
            s3Service.excluirArquivo(arquivo.getCaminho());
            arquivoRepository.delete(arquivo);

            log.info("Arquivo com id {} foi completamente excluído.", id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Ocorreu um erro ao excluir o arquivo com id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar a exclusão do arquivo.");
        }
    }
}