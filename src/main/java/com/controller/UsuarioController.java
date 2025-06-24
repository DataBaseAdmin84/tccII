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

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null || usuarioLogado.getPerfil() == null) {
            return "redirect:/";
        }

        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios";
    }

    @GetMapping("/cadastrousuario")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "cadastrousuario";
    }

    @PostMapping("/cadastrousuario")
    public String salvar(@ModelAttribute UsuarioDTO usuarioDTO, Model model, HttpSession session) {
        try {
            String email = usuarioDTO.getEmail();
            if(usuarioService.validar(email)) {
                model.addAttribute("erro", "Email já esta sendo usado por outro aluno.");
                return "erro";
            }
            Usuario usuario = usuarioDTO.create();
            usuario.setDataInclusao(new Date());

            usuarioRepository.save(usuario);
            session.setAttribute("usuarioLogado", usuario);
            model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");
            return "redirect:/painelprincipal";

        } catch (RuntimeException e) {
            model.addAttribute("erro", "Erro ao salvar o usuario.");
            return "erro";
        }
    }

    @GetMapping("/usuario/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuarioDTO);
            return "cadastrousuario";
        } catch (RuntimeException e) {
            return "redirect:/usuarios";
        }
    }

    @GetMapping("/usuario/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "formusuario";
    }

    @GetMapping("/usuario/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirPorId(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
