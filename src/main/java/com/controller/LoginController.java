package com.controller;

import com.dto.LoginDTO;
import com.enums.PerfilUsuario;
import com.model.Usuario;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String getLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@ModelAttribute("login") LoginDTO loginDTO, Model model, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginDTO.getLogin(), loginDTO.getSenha());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/painelprincipal";
        }

        model.addAttribute("erro", "Credenciais inválidas. Tente novamente.");
        return "redirect:/login";
    }
}
