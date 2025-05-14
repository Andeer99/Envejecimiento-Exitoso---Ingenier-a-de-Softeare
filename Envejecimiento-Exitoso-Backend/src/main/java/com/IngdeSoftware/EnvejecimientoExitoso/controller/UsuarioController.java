package com.IngdeSoftware.EnvejecimientoExitoso.controller;

import com.IngdeSoftware.EnvejecimientoExitoso.model.Usuario;
import com.IngdeSoftware.EnvejecimientoExitoso.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // 1) Listado de usuarios
    @GetMapping
    public String list(Model model) {
        model.addAttribute("usuarios", service.findAll());
        return "user-list";     // dist/user-list.html
    }

    // 2) Formulario para crear uno nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "user-form";     // dist/user-form.html
    }

    // 3) Guardar (POST tanto para nuevo como edición)
    @PostMapping
    public String guardar(@ModelAttribute Usuario usuario) {
        service.save(usuario);
        return "redirect:/usuarios";
    }

    // 4) Editar existente
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.findById(id));
        return "user-form";
    }

    // 5) Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/usuarios";
    }
}
