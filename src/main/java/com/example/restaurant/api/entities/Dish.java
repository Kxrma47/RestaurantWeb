package com.example.restaurant.api.entities;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Dish {
    @Getter
    @Setter
    @Expose
    private String dishName;
    @Getter
    @Setter
    @Expose
    private String quantity;
    @Getter
    @Setter
    @Expose
    private String price;
    @Getter
    @Setter
    @Expose
    private String difficulty;

    public Dish(){}

    public Dish(String dishName, String quantity, String price, String difficulty) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Dish dish = (Dish) obj;
        return Objects.equals(dishName, dish.dishName);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "dishName='" + dishName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishName);
    }
}
