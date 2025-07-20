package com.controller;

import com.dataUtil.DataUtils;
import com.model.Arquivo;
import com.model.ArquivoAula;
import com.model.Aula;
import com.repository.ArquivoAulaRepository;
import com.repository.ArquivoRepository;
import com.repository.AulaRepository;
import com.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Controller
public class UploadController {
    private static final Logger log = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    AulaRepository aulaRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoAulaRepository arquivoAulaRepository;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadArquivo(@RequestParam("aulaId") Long aulaId,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("titulo") String titulo,
                                RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Por favor, selecione um arquivo para o upload.");
            return "redirect:/aula/editar/" + aulaId;
        }

        try {
            Optional<Aula> aulaOpt = aulaRepository.findById(aulaId);
            if (aulaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Aula não encontrada.");
                return "redirect:/cursos";
            }
            Aula aula = aulaOpt.get();

            String s3Url = s3Service.uploadArquivo(file);

            Arquivo arquivo = new Arquivo();
            arquivo.setNome(titulo);
            arquivo.setCaminho(s3Url);
            arquivo.setTamanho(file.getSize());
            arquivo.setDataUpload(DataUtils.dateToLocalDate(new Date()));
            arquivo.setTipo(" ");//TODO

            var arq = arquivoRepository.save(arquivo);
            var arquivoAula = new ArquivoAula();
            arquivoAula.setArquivo(arq);
            arquivoAula.setAula(aula);

            arquivoAulaRepository.save(arquivoAula);
            return "redirect:aula/editar/" + aulaId;

        } catch (IOException e) {
            log.error("Erro ao processar arquivo");
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o arquivo: " + e.getMessage());
        }
        return "redirect:aula/editar/" + aulaId;
    }
}
