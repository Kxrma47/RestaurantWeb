package com.example.restaurant.api.services;

import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    DishRepository dishRepository;
    public void add(Dish dish) {
        dishRepository.addNewDish(new Dish(dish.getDishName(), dish.getQuantity(),
                dish.getPrice(), dish.getDifficulty()));
    }

    public void delete(Dish dish) {
        dishRepository.deleteDish(dish);
    }

    public List<Dish> getDishList() {
        return dishRepository.getDishList();
    }
}
