package com.example.restaurant.api.controllers;

import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.entities.Order;
import com.example.restaurant.api.exceptions.customExceptions.BadDishNameException;
import com.example.restaurant.api.exceptions.customExceptions.BadDishQuantityException;
import com.example.restaurant.api.interfaces.UserInterface;
import com.example.restaurant.api.repository.DishRepository;
import com.example.restaurant.api.securityModul.Security;
import com.example.restaurant.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;


@EnableScheduling
@Controller
public class UserController implements UserInterface {
    @Autowired
    UserService userService;
    @Autowired
    DishRepository dishRepository;
    String error = "";
    public String dashboard(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("user")) {
                return "user/userDashboard";
            } else {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/security/login";
    }
    @Override
    public String createOrder(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("user")) {
                model.addAttribute("dishes", userService.getDishList());
                if (!error.isEmpty()) {
                    model.addAttribute("error", error);
                    error = "";
                }
                return "user/createOrder";
            } else {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public String createOrder(Dish dishForm, Model model) {
        model.addAttribute("error", null);
        if (!dishRepository.findByName(dishForm)) {
            error = "There's no such dish";
            return "redirect:/user/createOrder";
        }
        try {
            userService.createOrder(dishForm);
        } catch (BadDishNameException badDishNameException) {
            error = "Invalid dish name";
        } catch (BadDishQuantityException badDishQuantityException) {
            error = "Invalid number of dishes";
        }
        return "redirect:/user/createOrder";
    }

    @Override
    public String orders(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("user")) {
                boolean anyCookingOrders = Security.getCurrentUser().getTransferOrdersList().stream().anyMatch(order -> order.getStatus().equals("Готовится"));
                boolean anyReadyOrders = Security.getCurrentUser().getTransferOrdersList().stream().anyMatch(order -> order.getStatus().equals("Заказ готов"));
                List<Dish> dishes = userService.getDishList();
                boolean anyDishes = !dishes.isEmpty();
                model.addAttribute("anyDishes", anyDishes);
                model.addAttribute("anyCookingOrders", anyCookingOrders);
                model.addAttribute("orders", Security.getCurrentUser().getTransferOrdersList());
                model.addAttribute("anyReadyOrders", anyReadyOrders);
                return "user/orders";
            } else {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public String finishOrder() {
        return userService.finishOrder();
    }

    @Override
    public String pay() {
         userService.pay();
         return "redirect:/user/orders";
    }

    @Override
    public String stopCook(int orderId) {
        userService.stopCook(orderId - 1);
        return "redirect:/user/orders";
    }

    @Override
    public String changeOrder(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("user")) {
                model.addAttribute("dishes", userService.getDishList());
                return "user/changeOrder";
            } else {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/security/login";
    }

    @Override
    public String pauseCook(int orderId) {
        userService.pauseCook(orderId - 1);
        return "redirect:/user/changeOrder";
    }

    @Override
    public String changeOrder(Dish dish, Model model) {
        userService.changeOrder(dish);
        return "redirect:/user/orders";
    }

    @Override
    public String menuUser(Model model) {
        if (Security.checkForAuth()) {
            if (Security.checkForCurrentAcc().equals("user")) {
                model.addAttribute("dishes", userService.getDishList());
                return "user/menuUser";
            } else {
                return "admin/adminDashboard";
            }
        }
        return "redirect:/security/login";
    }

}
