package com.example.restaurant.api.controllers;

import com.example.restaurant.api.entities.Admin;
import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.exceptions.customExceptions.*;
import com.example.restaurant.api.interfaces.AdminInterface;
import com.example.restaurant.api.securityModul.Security;
import com.example.restaurant.api.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AdminController implements AdminInterface {

    @Autowired
    AdminService adminService;
    @Override
    public String dashboard(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("admin")) {
                model.addAttribute("income", Admin.getIncome());
                return "admin/adminDashboard";
            } else {
                return "user/userDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public String add(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("admin")) {
                model.addAttribute("dishForm", new Dish());
                return "admin/add";
            } else {
                return "user/userDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public void add(@ModelAttribute("dishForm") Dish dishForm, Model model) throws BadDishNameException {
        model.addAttribute("error", null);
        try {
            adminService.add(dishForm);

        } catch (BadDishNameException badDishNameException) {
            model.addAttribute("error", "Некорректное название блюда");
        } catch (BadDishQuantityException badDishQuantityException) {
            model.addAttribute("error", "Некорректное количество");
        } catch (BadDishPriceException badDishPriceException) {
            model.addAttribute("error", "Некорректная цена");
        } catch (BadDishDifficultyException badDishDifficultyException) {
            model.addAttribute("error", "Некорректное время приготовления");
        }
    }

    @Override
    public String delete(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("admin")) {
                model.addAttribute("dishForm", new Dish());
                return "admin/delete";
            } else {
                return "user/userDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public void delete(Dish dishForm, Model model) {
        model.addAttribute("error", null);
        try {
           adminService.delete(dishForm);
        } catch (BadDishNameException badDishNameException) {
            model.addAttribute("error", "Неправильное имя блюда");
        } catch (NotFoundDishException notFoundDishException) {
            model.addAttribute("error", "Такого блюда в меню не существует");
        }
    }

    @Override
    public String menuAdmin(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("admin")) {
                model.addAttribute("dishes", adminService.getDishList());
                return "admin/menuAdmin";
            } else {
                return "user/userDashboard";
            }
        }
        return "redirect:/security/login";
    }
}
