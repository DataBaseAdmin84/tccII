package com.controller;

import com.dto.LoginDTO;
import com.model.Usuario;
import com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String login(@ModelAttribute("login") LoginDTO loginDTO, Model model, HttpSession session) {
        var autenticar = usuarioService.autenticar(loginDTO);
        try {
            if (autenticar) {
                Usuario usuario = usuarioService.findUser(loginDTO);
                session.setAttribute("usuarioLogado", usuario);
                return "redirect:/painelprincipal";
            }else{
                model.addAttribute("erro", "Credenciais inválidas. Tente novamente.");
                return "erro";
            }
        } catch (Exception e) {
            model.addAttribute("erro", "Erro no login: " + e.getMessage());
            return "erro";
        }
    }
}
