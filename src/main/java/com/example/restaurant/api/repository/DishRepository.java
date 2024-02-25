package com.example.restaurant.api.repository;

import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.exceptions.advice.ExceptionsChecker;
import com.example.restaurant.api.exceptions.customExceptions.*;
import com.example.restaurant.api.securityModul.Security;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class DishRepository {
    List<Dish> dishList = new ArrayList<>();

    public void clearAll() {
        dishList.clear();
    }
    public List<Dish> findAll() {
        return dishList;
    }
    public void saveAll(List<Dish> dishes) {
        dishList.addAll(dishes);
    }
    public boolean findByName(Dish dish) {
        return dishList.stream().anyMatch(x -> x.getDishName().equals(dish.getDishName()));
    }
    public Dish findDish(Dish dish) {
        return dishList.stream().filter(x -> x.getDishName().equals(dish.getDishName())).findFirst().orElse(null);
    }
    public void addNewDish(Dish dish) throws BadDishNameException {
        ExceptionsChecker.checkForAdd(dish);
        Dish ex = dishList.stream().filter(x -> x.getDishName().equals(dish.getDishName())).findFirst().orElse(null);
        if (ex != null) {
            ex.setQuantity(String.valueOf(Integer.parseInt(ex.getQuantity()) + Integer.parseInt(dish.getQuantity())));
        } else {
            dishList.add(dish);
        }
    }

    public void deleteDish(Dish dish) {
        ExceptionsChecker.checkForRemove(dish);
        Dish ex = dishList.stream().filter(x -> x.getDishName().equals(dish.getDishName())).findFirst().orElse(null);
        if (ex != null) {
            dishList.remove(dish);
        } else {
            throw new NotFoundDishException();
        }
    }
    public void createOrder(Dish dish) {
        ExceptionsChecker.checkForAddOrder(dish);
        Dish dishInMemory = findDish(dish);
        if (Integer.parseInt(dish.getQuantity()) <= Integer.parseInt(dishInMemory.getQuantity())) {
            boolean existingDish = false;
            for (Dish existingDishInOrder : Security.getCurrentUser().getDishesToOrder()) {
                if (existingDishInOrder.getDishName().equals(dishInMemory.getDishName())) {
                    int newQuantity = Integer.parseInt(existingDishInOrder.getQuantity()) +
                            Integer.parseInt(dish.getQuantity());
                    existingDishInOrder.setQuantity(String.valueOf(newQuantity));
                    existingDish = true;
                    break;
                }
            }
            if (!existingDish) {
                Dish dishToAdd = new Dish(
                        dishInMemory.getDishName(),
                        dish.getQuantity(),
                        dishInMemory.getPrice(),
                        dishInMemory.getDifficulty()
                );
                Security.getCurrentUser().getDishesToOrder().add(dishToAdd);
            }
            dishInMemory.setQuantity(String.valueOf(
                    Integer.parseInt(dishInMemory.getQuantity()) - Integer.parseInt(dish.getQuantity())));

            if (Integer.parseInt(dishInMemory.getQuantity()) == 0) {
                deleteDish(dishInMemory);
            }
        }

    }
}
