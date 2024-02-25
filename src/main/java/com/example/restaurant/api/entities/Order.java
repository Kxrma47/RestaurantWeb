package com.example.restaurant.api.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Order {
    @Getter
    @Setter
    List<Dish> dishesToOrder = new ArrayList<>();

    @Getter
    @Setter
    String status = "";

    public Order(){}

    public Order(List<Dish> dishesToOrder, String status) {
        this.dishesToOrder = dishesToOrder;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "dishesToOrder=" + dishesToOrder +
                ", status='" + status + '\'' +
                '}';
    }
}
