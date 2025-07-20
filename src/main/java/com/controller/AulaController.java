package com.controller;

import com.dto.ArquivoDTO;
import com.dto.AulaDTO;
import com.dto.UsuarioDTO;
import com.model.Arquivo;
import com.model.Aula;
import com.model.Usuario;
import com.repository.ArquivoRepository;
import com.repository.AulaRepository;
import com.repository.CursoRepository;
import com.service.S3Service;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AulaController {
    private static final Logger log = LoggerFactory.getLogger(AulaController.class);
    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    AulaRepository aulaRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private S3Service s3Service;

    @GetMapping("/aula/novo")
    public String preencheFormulario(@ModelAttribute AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            var usuario = (Usuario) session.getAttribute("usuarioLogado");
            model.addAttribute("aula", aulaDTO);
            model.addAttribute("usuarioLogado", usuario);
            return "preparacaoaula";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/cursos";
    }

    @PostMapping("/aula/salvar")
    public String salvarAula(@ModelAttribute AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            if(aulaDTO.getId() != null)
                editarAula(aulaDTO.getId(), aulaDTO, model, session);

            var curso = cursoRepository.findById(aulaDTO.getIdCurso());
            Aula aula = AulaDTO.toModel(aulaDTO);
            curso.ifPresent(aula::setCurso);

            aulaRepository.save(aula);
            model.addAttribute("aula", AulaDTO.toDto(aula));
            model.addAttribute("cursoId", aula.getCurso().getId());
            model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));
            return "preparacaoaula";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao preencher formulário de aula "+e.getLocalizedMessage());
            log.error(e.getLocalizedMessage());
        }
        return "redirect:/preparacaoaula";
    }

    @GetMapping("/aula/editar/{id}")
    public String editarAula(@PathVariable("id") Long id, AulaDTO aulaDTO, Model model, HttpSession session) {
        try {
            Optional<Aula> aulaOptional = aulaRepository.findById(id);

            if (aulaOptional.isPresent()) {
                Aula aula = aulaOptional.get();
                aulaDTO = AulaDTO.toDto(aula);

                var listArquivos = new ArrayList<ArquivoDTO>();
                for(Arquivo arquivo: aulaOptional.get().getArquivos()){
                    var dto = ArquivoDTO.toDto(arquivo);
                    listArquivos.add(dto);
                }
                aulaDTO.setArquivos(listArquivos);

                model.addAttribute("aula", aulaDTO);
                model.addAttribute("cursoId", aula.getCurso().getId());
                model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

                return "preparacaoaula";
            } else {
                return "redirect:/cursos";
            }
        } catch (Exception e) {
            log.error("Erro ao carregar aula para edição: {}", e.getMessage());
            return "redirect:/cursos";
        }
    }


    @PostMapping("/material/upload")
    public String uploadArquivo(@RequestParam("aulaId") Long aulaId,
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("titulo") String titulo,
                                RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Por favor, selecione um arquivo para o upload.");
            return "redirect:/aula/editar/" + aulaId;
        }

        try {
            // 1. Encontrar a Aula no banco de dados.
            Optional<Aula> aulaOpt = aulaRepository.findById(aulaId);
            if (aulaOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Aula não encontrada.");
                return "redirect:/cursos"; // Redireciona para uma página segura
            }
            Aula aula = aulaOpt.get();

            // 2. Upload do arquivo para o S3.
            String s3Url = s3Service.uploadArquivo(file);

            // 3. Criar uma nova entidade Arquivo.
            Arquivo novoArquivo = new Arquivo();
            novoArquivo.setNome(titulo); // Use o nome do formulário para o arquivo.
            novoArquivo.setCaminho(s3Url); // Salva o URL do S3 no campo 'caminho'.
            novoArquivo.setTamanho(file.getSize());
            novoArquivo.setTipo("ARQUIVO");

            // 4. Salvar a nova entidade Arquivo no banco de dados.
            // Isso é necessário porque o JPA precisa de um ID para a entidade antes de poder
            // adicioná-la à lista da Aula para o relacionamento ManyToMany.
            arquivoRepository.save(novoArquivo);

            // 5. Adicionar o novo Arquivo à lista de arquivos da Aula.
            List<Arquivo> arquivosDaAula = aula.getArquivos();
            arquivosDaAula.add(novoArquivo);
            aula.setArquivos(arquivosDaAula);

            // 6. Salvar a Aula atualizada para persistir o relacionamento.
            aulaRepository.save(aula);

            redirectAttributes.addFlashAttribute("msg", "Arquivo '" + titulo + "' adicionado com sucesso!");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar o arquivo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro inesperado durante o upload: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/aula/editar/" + aulaId;
    }
}
