package com.example.restaurant.app;

import com.example.restaurant.api.util.Saver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.restaurant.api.securityModul")
@ComponentScan("com.example.restaurant.api.repository")
@ComponentScan("com.example.restaurant.api.interfaces")
@ComponentScan("com.example.restaurant.api.controllers")
@ComponentScan("com.example.restaurant.api.services")
@ComponentScan("com.example.restaurant.api.util")
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

}
