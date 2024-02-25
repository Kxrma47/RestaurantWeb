package com.example.restaurant.api.services;

import com.example.restaurant.api.entities.Admin;
import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.entities.Order;
import com.example.restaurant.api.entities.User;
import com.example.restaurant.api.exceptions.advice.ExceptionsChecker;
import com.example.restaurant.api.repository.DishRepository;
import com.example.restaurant.api.repository.UserRepository;
import com.example.restaurant.api.securityModul.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.restaurant.api.util.Saver.saveIncome;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DishRepository dishRepository;
    Map<Integer, Order> mapOrders = new HashMap<>();
    Map<Integer, Thread> mapThreads = new HashMap<>();
    int tempId = 0;
    public boolean checkForRegistration(User user) {
        return userRepository.checkForRegistration(user);
    }

    public void addPerson(User user) {
        userRepository.addPerson(user);
    }

    public boolean checkForLogin(User user) {
        return userRepository.checkForLogin(user);
    }

    public User findByNameAndPassword(User userForm) {
        return userRepository.findByNameAndPass(userForm);
    }

    public void createOrder(Dish dish) {
        dishRepository.createOrder(dish);
    }
    public List<Dish> getDishList() {
        return dishRepository.getDishList();
    }

    public void pay() {
        int income = 0;
        for (Order prices : Security.getCurrentUser().getTransferOrdersList()) {
            if (prices.getStatus().equals("Заказ готов")) {
                income += Integer.parseInt(prices.getDishesToOrder().get(0).getPrice());
                prices.setStatus("Оплачен");
            }
        }
        Admin.setIncome(Admin.getIncome() + income);
        saveIncome();
        System.out.println(Admin.getIncome());
    }
    public String finishOrder() {
        if (!Security.getCurrentUser().getDishesToOrder().isEmpty()) {
            List<Dish> ret = new ArrayList<>(Security.getCurrentUser().getDishesToOrder());
            Order order = new Order(ret, "Принят");
            Security.getCurrentUser().getTempOrdersList().add(order);
            Security.getCurrentUser().getAllOrders().add(order);
            Dish copyDish = new Dish();
            copyDish.setPrice(String.valueOf(Security.getCurrentUser().getDishesToOrder().stream().mapToInt(dish -> Integer.parseInt(dish.getPrice()) * Integer.parseInt(dish.getQuantity())).sum()));
            copyDish.setDifficulty(String.valueOf(Security.getCurrentUser().getDishesToOrder().stream().mapToInt(dish -> Integer.parseInt(dish.getDifficulty()) * Integer.parseInt(dish.getQuantity())).sum()));
            List<Dish> temp = new ArrayList<>();
            temp.add(copyDish);
            Order tranferOrder = new Order(temp,"Принят");
            Security.getCurrentUser().getTransferOrdersList().add(tranferOrder);
            mapOrders.put(Security.getCurrentUser().getIdOfOrder(), tranferOrder);
            Security.getCurrentUser().setIdOfOrder(Security.getCurrentUser().getIdOfOrder() + 1);
            startCook();
        }
        Security.getCurrentUser().getDishesToOrder().clear();
        Security.getCurrentUser().getTempOrdersList().clear();
        return "redirect:/user/orders";
    }
    public void startCook() {
        for (int i = 0; i < Security.getCurrentUser().getIdOfOrder(); i++) {
            int cookTime = Security.getCurrentUser().getTempOrdersList().get(0).getDishesToOrder()
                    .stream()
                    .mapToInt(dish -> Integer.parseInt(dish.getDifficulty()) * Integer.parseInt(dish.getQuantity()))
                    .sum();
            Thread thread = getThread(i, cookTime);
            mapThreads.put(i, thread);
            thread.start();
        }
    }

    private Thread getThread(int i, int cookTime) {
        return new Thread(() -> {
            try {
                if (!mapOrders.get(i).getStatus().equals("Отменён")
                        && !mapOrders.get(i).getStatus().equals("Заказ готов")
                        && !mapOrders.get(i).getStatus().equals("Готовится")
                        && !mapOrders.get(i).getStatus().equals("Изменён")
                        && !mapOrders.get(i).getStatus().equals("Оплачен")) {
                    mapOrders.get(i).setStatus("Готовится");
                    Thread.sleep(cookTime * 1000L);
                    mapOrders.get(i).setStatus("Заказ готов");
                }
            } catch (InterruptedException ignored) {}
        });
    }

    public void stopCook(int orderId) {
        mapThreads.get(orderId).interrupt();
        mapOrders.get(orderId).setStatus("Отменён");
        for (Dish dish : Security.getCurrentUser().getAllOrders().get(orderId).getDishesToOrder()) {
            dishRepository.addNewDish(dish);
        }

    }
    public void pauseCook(int orderId) {
        tempId = orderId;
        mapThreads.get(tempId).interrupt();
        mapOrders.get(tempId).setStatus("Изменён");
    }

    public void changeOrder(Dish dish) {
        ExceptionsChecker.checkForAddOrder(dish);
        List<Dish> chosen = Security.getCurrentUser().getAllOrders().get(tempId).getDishesToOrder();
        Dish temp = dishRepository.findDish(dish);

        if (Integer.parseInt(dish.getQuantity()) <= Integer.parseInt(temp.getQuantity())) {

            boolean ex = false;

            for (Dish existingDishInOrder : chosen) {
                if (existingDishInOrder.getDishName().equals(temp.getDishName())) {
                    int newQuantity = Integer.parseInt(existingDishInOrder.getQuantity()) +
                            Integer.parseInt(dish.getQuantity());
                    existingDishInOrder.setQuantity(String.valueOf(newQuantity));
                    ex = true;
                    break;
                }
            }
            if (!ex) {
                Dish dishToAdd = new Dish(
                        temp.getDishName(),
                        dish.getQuantity(),
                        temp.getPrice(),
                        temp.getDifficulty()
                );
                chosen.add(dishToAdd);
            }
            temp.setQuantity(String.valueOf(
                    Integer.parseInt(temp.getQuantity()) - Integer.parseInt(dish.getQuantity())));

            if (Integer.parseInt(temp.getQuantity()) == 0) {
                dishRepository.deleteDish(temp);
            }
        }
        Security.getCurrentUser().getDishesToOrder().addAll(chosen);
        finishOrder();
    }
}
