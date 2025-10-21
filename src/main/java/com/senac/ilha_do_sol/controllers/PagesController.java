package com.senac.ilha_do_sol.controllers;

import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.repositories.UsersRepository;
import com.senac.ilha_do_sol.services.QuartosService;
import com.senac.ilha_do_sol.services.ReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PagesController {

    @Autowired
    private QuartosService quartosService;

    @Autowired
    private ReservasService reservasService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        model.addAttribute("quartos", quartos);
        return "Home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "AdmHome";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        model.addAttribute("quartos", quartos);
        return "Home";
    }

    @GetMapping("/quartos")
    public String quartos(Model model) {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        model.addAttribute("quartos", quartos);
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
    public String perfil(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = usersRepository.findByUsername(username).orElseThrow();
        model.addAttribute("user", user);
        return "Perfil";
    }

    @GetMapping("/reservas")
    public String reservas(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = usersRepository.findByUsername(username).orElseThrow();
        List<Reservas> reservas = reservasService.buscarReservasPorUsuario(user);
        model.addAttribute("reservas", reservas);
        return "Reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id) {
        reservasService.cancelarReserva(id);
        return "redirect:/reservas";
    }

}
