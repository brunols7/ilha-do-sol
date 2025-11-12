package com.senac.ilha_do_sol.controllers;

import com.senac.ilha_do_sol.dto.UserDTO;
import com.senac.ilha_do_sol.entities.HotelInfo;
import com.senac.ilha_do_sol.entities.Quartos;
import com.senac.ilha_do_sol.entities.Reservas;
import com.senac.ilha_do_sol.entities.Role;
import com.senac.ilha_do_sol.entities.Users;
import com.senac.ilha_do_sol.repositories.UsersRepository;
import com.senac.ilha_do_sol.services.HotelInfoService;
import com.senac.ilha_do_sol.services.QuartosService;
import com.senac.ilha_do_sol.services.ReservasService;
import com.senac.ilha_do_sol.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PagesController {

    @Autowired
    private QuartosService quartosService;

    @Autowired
    private ReservasService reservasService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private HotelInfoService hotelInfoService;

    @GetMapping("/")
    public String index(Model model) {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        HotelInfo hotelInfo = hotelInfoService.buscarInformacaoPrincipal();
        model.addAttribute("quartos", quartos);
        model.addAttribute("hotelInfo", hotelInfo);
        return "Home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "AdmHome";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Quartos> quartos = quartosService.buscarTodosQuartos();
        HotelInfo hotelInfo = hotelInfoService.buscarInformacaoPrincipal();
        model.addAttribute("quartos", quartos);
        model.addAttribute("hotelInfo", hotelInfo);
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

    @PostMapping("/cadastro")
    public String cadastrar(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String cpf,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {
        try {
            UserDTO userDTO = new UserDTO(username, email, cpf, password, Role.USER);
            usersService.createUser(userDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso!");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @GetMapping("/perfil")
    public String perfil(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }
        String email = auth.getName();
        Users user = usersRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "Perfil";
    }

    @GetMapping("/reservas")
    public String reservas(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }
        String email = auth.getName();
        Users user = usersRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
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
