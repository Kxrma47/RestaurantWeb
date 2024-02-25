package com.example.restaurant.api.exceptions.advice;

import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.exceptions.customExceptions.BadDishDifficultyException;
import com.example.restaurant.api.exceptions.customExceptions.BadDishNameException;
import com.example.restaurant.api.exceptions.customExceptions.BadDishPriceException;
import com.example.restaurant.api.exceptions.customExceptions.BadDishQuantityException;
import com.example.restaurant.api.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExceptionsChecker {
    public static void checkForAdd(Dish dish) {
        if (dish.getDishName().isEmpty() || dish.getDishName().length() > 30) {
            throw new BadDishNameException();
        }
        if (dish.getPrice().isEmpty() || dish.getPrice().matches(".*[^0-9].*")
                || Integer.parseInt(dish.getPrice()) < 1 || Integer.parseInt(dish.getPrice()) > 1000) {
            throw new BadDishPriceException();
        }
        if (dish.getQuantity().isEmpty() || dish.getQuantity().matches(".*[^0-9].*")
                || Integer.parseInt(dish.getQuantity()) < 1 || Integer.parseInt(dish.getQuantity()) > 5) {
            throw new BadDishQuantityException();
        }
        if (dish.getDifficulty().isEmpty() || dish.getDifficulty().matches(".*[^0-9].*")
                || Integer.parseInt(dish.getDifficulty()) < 2 || Integer.parseInt(dish.getDifficulty()) > 15) {
            throw new BadDishDifficultyException();
        }
    }
    public static void checkForRemove(Dish dish) {
        if (dish.getDishName().isEmpty() || dish.getDishName().length() > 30) {
            throw new BadDishNameException();
        }
    }
    public static void checkForAddOrder(Dish dish) {
        if (dish.getDishName().isEmpty() || dish.getDishName().length() > 30) {
            throw new BadDishNameException();
        }
        if (dish.getQuantity().isEmpty() || dish.getQuantity().matches(".*[^0-9].*")
                || Integer.parseInt(dish.getQuantity()) < 1 || Integer.parseInt(dish.getQuantity()) > 5) {
            throw new BadDishQuantityException();
        }
    }
}
