package com.example.restaurant.api.interfaces;

import com.example.restaurant.api.entities.Dish;

import org.apache.coyote.BadRequestException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public interface AdminInterface {
    @GetMapping("/dashboard")
    String dashboard(Model model);

    @GetMapping("/add")
    String add(Model model);
    @PostMapping("/add")
    void add(@ModelAttribute("dishForm") Dish dishForm, Model model) throws BadRequestException;

    @GetMapping("/delete")
    String delete(Model model);
    @PostMapping("/delete")
    void delete(@ModelAttribute("dishForm") Dish dishForm, Model model);

    @GetMapping("/menuAdmin")
    String menuAdmin(Model model);

}
