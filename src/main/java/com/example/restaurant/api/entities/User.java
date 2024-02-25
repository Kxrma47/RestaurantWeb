package com.example.restaurant.api.entities;

import com.example.restaurant.api.util.Saver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class User {

    BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
    @Getter
    @Setter
    private String login;
    @Getter
    private String password;

    @Getter
    @Setter
    List<Dish> dishesToOrder = new ArrayList<>();
    @Getter
    @Setter
    List<Order> tempOrdersList = new ArrayList<>();
    @Getter
    @Setter
    List<Order> allOrders = new ArrayList<>();
    @Getter
    @Setter
    List<Order> transferOrdersList = new ArrayList<>();
    @Getter
    @Setter
    int idOfOrder = 0;
    public User() {}

    public User(String login, String password, List<Dish> dishesToOrder) {
        this.login = login;
        if (password.length() > 20) {
            this.password = password;
        } else {
            this.password  = cryptPasswordEncoder.encode(password);
        }
        System.out.println(this.password);
        this.dishesToOrder = dishesToOrder;
    }
    public boolean checkPassword(String password) {
        return cryptPasswordEncoder.matches(password, this.password);
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
