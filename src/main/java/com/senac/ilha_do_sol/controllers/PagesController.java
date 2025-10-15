package com.senac.ilha_do_sol.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/")
    public String index() {
        return "Home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "AdmHome";
    }

    @GetMapping("/home")
    public String home() {
        return "Home";
    }

    @GetMapping("/quartos")
    public String quartos() {
        return "QuartosSuites";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "Cadastro";
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/perfil")
    public String perfil(){
        return "Perfil";
    }

    @GetMapping("/reservas")
    public String reservas(){
        return "Reservas";
    }

}
