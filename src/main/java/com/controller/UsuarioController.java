package com.controller;

import com.dto.LoginDTO;
import com.dto.UsuarioDTO;
import com.enums.PerfilUsuario;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String mostrarLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@ModelAttribute("login") LoginDTO loginDTO, Model model, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginDTO.getLogin(), loginDTO.getSenha());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogado", usuario);

            if (usuario.getPerfil() == null) {
                model.addAttribute("erro", "Usuário sem perfil definido.");
                return "login";
            } else if (usuario.getPerfil().equals(PerfilUsuario.PROFESSOR.getCodigo())) {
                return "redirect:/professor/home";
            } else if (usuario.getPerfil().equals(PerfilUsuario.ALUNO.getCodigo())) {
                return "redirect:/aluno/home";
            } else {
                model.addAttribute("erro", "Perfil de usuário desconhecido.");
                return "login";

            }
        }
        return "redirect:/logi";
    }

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
    public String salvar(@ModelAttribute UsuarioDTO usuarioDTO, Model model) {
        try {
            String email = usuarioDTO.getEmail();
            if(usuarioService.validar(email)) {
                model.addAttribute("erro", "Email já esta sendo usado por outro aluno.");
                return "erro";
            }
            Usuario usuario = usuarioDTO.create();
            usuario.setDataInclusao(new Date());

            usuarioRepository.save(usuario);
            model.addAttribute("sucesso", "Usuário cadastrado com sucesso!");

            if(usuario.getPerfil().equals(PerfilUsuario.PROFESSOR.getCodigo())){
                return "professor/home";

            } else if (usuario.getPerfil().equals(PerfilUsuario.ALUNO.getCodigo())) {
                return "aluno/home";

            } else {
                return "templates/erro";
            }

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
