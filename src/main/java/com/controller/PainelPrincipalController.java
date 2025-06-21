package com.controller;


import com.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PainelPrincipalController {

    @GetMapping("/painelprincipal")
    public String getTelaPrincipal(Model model, HttpSession session) {
        var user = session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", user);

        return "painelprincipal";
    }
}
