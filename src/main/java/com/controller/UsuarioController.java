package com.controller;

import com.dto.UsuarioDTO;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastrousuario")
    public String salvar(@ModelAttribute UsuarioDTO usuarioDTO, Model model, HttpSession session) {
        try {
            String email = usuarioDTO.getEmail();
            String login = usuarioDTO.getLogin();
            boolean existe = usuarioRepository.existsByEmailAndLogin(email, login);
            if (existe) {
                model.addAttribute("erro", "E-mail já estão sendo usado por outro usuário.");
                model.addAttribute("usuario", usuarioDTO);
                return "cadastrousuario";
            }

            if (usuarioService.validarEmail(email)) {
                model.addAttribute("erro", "Email já está sendo usado por outro usuário.");
                model.addAttribute("usuario", usuarioDTO);
                return "cadastrousuario";
            }
            if (!usuarioService.validarLogin(login)) {
                model.addAttribute("erro", "Login já está sendo usado por outro usuário.");
                model.addAttribute("usuario", usuarioDTO);
                return "cadastrousuario";
            }



            Usuario usuario = UsuarioDTO.toModel(usuarioDTO);
            usuario.setDataInclusao(new Date());
            usuarioRepository.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
            model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/painelprincipal";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar o usuário.");
            return "erro";
        }
    }

    @GetMapping("/usuario/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            if(usuario.isPresent()){
                var dto = UsuarioDTO.toDto(usuario.get());
                model.addAttribute("usuario", dto);
            }
            return "cadastrousuario";
        } catch (RuntimeException e) {
            model.addAttribute("erro", "Erro ao editar Usuário.");
            return "erro";
        }
    }

    @GetMapping("/usuario/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "cadastrousuario";
    }

    @GetMapping("/usuario/excluir/{id}")
    public String excluir(@PathVariable Long id, HttpSession session) {
        try {
            var usuarioOpt = usuarioRepository.findById(id);
            if(usuarioOpt.isPresent()){
                usuarioService.removeVincululos(usuarioOpt.get());
                usuarioRepository.deleteById(id);
                session.invalidate();
            }
            return "redirect:/";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
