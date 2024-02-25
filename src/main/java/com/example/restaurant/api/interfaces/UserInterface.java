package com.example.restaurant.api.interfaces;

import com.example.restaurant.api.entities.Dish;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
public interface UserInterface {

    @GetMapping("/userDashboard")
    String dashboard(Model model);

    @GetMapping("/createOrder")
    String createOrder(Model model);

    @PostMapping("/createOrder")
    String createOrder(@ModelAttribute("dishForm") Dish dishForm, Model model);

    @GetMapping("/orders")
    String orders(Model model);

    @GetMapping("/finishOrder")
    String finishOrder();
    @GetMapping("/pay")
    String pay();

    @PostMapping("/stopCook")
    String stopCook(@RequestParam int orderId);

    @GetMapping("/changeOrder")
    String changeOrder(Model model);

    @PostMapping("/pauseCook")
    String pauseCook(@RequestParam int orderId) throws InterruptedException;
    @PostMapping("/changeOrder")
    String changeOrder(@ModelAttribute("dishForm") Dish dish, Model model);

    @GetMapping("/menuUser")
    String menuUser(Model model);
}
