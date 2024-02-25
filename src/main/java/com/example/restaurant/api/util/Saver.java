package com.example.restaurant.api.util;

import com.example.restaurant.api.dto.UserDTO;
import com.example.restaurant.api.entities.Admin;
import com.example.restaurant.api.entities.Dish;
import com.example.restaurant.api.entities.User;
import com.example.restaurant.api.repository.DishRepository;
import com.example.restaurant.api.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class Saver {
    @Autowired
    DishRepository dishRepository;
    @Autowired
    UserRepository userRepository;

    private final String usersFileName = "info/users.txt";
    private final String dishesFileName = "info/dishes.txt";
    private final String incomeFileName = "info/income.txt";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();;

    @Scheduled(fixedRate = 1000)
    private void save() {
        saveUsers();
        saveDishes();
    }

    public static void saveIncome() {
        long now = System.currentTimeMillis();
        long income = Admin.getIncome();
        String str = "Выручка на " + new Date(now) + " равна " + income + " рублей";
        try {
            Path filePath = Paths.get("info/income.txt");
            byte[] incomeBytes = str.getBytes();
            Files.write(filePath, incomeBytes);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            try {
                Path filePath = Paths.get(usersFileName);
                List<UserDTO> userDTOs = convertUsersToDTO(users);
                Files.write(filePath, gson.toJson(userDTOs).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private List<UserDTO> convertUsersToDTO(List<User> users) {
        return users.stream()
                .map(user -> new UserDTO(user.getLogin(), user.getPassword()))
                .collect(Collectors.toList());
    }
    public void createFiles() {
        try {
            Files.createDirectories(Paths.get("info"));
            Path usersPath = Paths.get(usersFileName);
            if (!Files.exists(usersPath)) {
                Files.createFile(usersPath);
            }
            Path dishesPath = Paths.get(dishesFileName);
            if (!Files.exists(dishesPath)) {
                Files.createFile(dishesPath);
            }
            Path incomePath = Paths.get(incomeFileName);
            if (!Files.exists(incomePath)) {
                Files.createFile(incomePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveDishes() {
        List<Dish> dishes = dishRepository.findAll();
        List<Dish> filteredDishes = dishes.stream()
                .filter(dish -> Integer.parseInt(dish.getQuantity()) > 0)
                .collect(Collectors.toList());

        if (!filteredDishes.isEmpty()) {
            try {
                Path filePath = Paths.get(dishesFileName);
                Files.write(filePath, gson.toJson(filteredDishes).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void read() {
        readUsers();
        readDishes();
        readIncome();
    }

    private void readIncome() {
        try {
            Path filePath = Paths.get(incomeFileName);
            if(Files.exists(filePath)) {
                String content = Files.readString(filePath);
                String[] parts = content.split("равна");
                String numberStr = parts[1].trim().split(" ")[0];
                long income = Long.parseLong(numberStr);
                Admin.setIncome(income);
                System.out.println("Выручка: " + income + " рублей");
            } else {

            }
        } catch (IOException ignored) {}

    }
    @SuppressWarnings("unchecked")
    private void readUsers() {
        try {
            String usersJson = new String(Files.readAllBytes(Paths.get(usersFileName)));
            UserDTO[] userDTOs = gson.fromJson(usersJson, UserDTO[].class);
            if (userDTOs != null && userDTOs.length > 0) {
                List<User> users = convertDTOsToUsers(Arrays.asList(userDTOs));
                userRepository.saveAll(users);
                System.out.println(userRepository.findAll());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<User> convertDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .map(userDTO -> new User(userDTO.getLogin(), userDTO.getPassword(), new ArrayList<>()))
                .collect(Collectors.toList());
    }
    @SuppressWarnings("unchecked")
    private void readDishes() {
        try (FileReader reader = new FileReader(dishesFileName)) {
            Dish[] dishes = gson.fromJson(reader, Dish[].class);
            if (dishes != null && dishes.length > 0) {
                dishRepository.saveAll(Arrays.asList(dishes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000)
    private void cleanDishesFile() {
        try (FileReader reader = new FileReader(dishesFileName)) {
            List<Dish> dishesFromFile = Arrays.stream(gson.fromJson(reader, Dish[].class))
                    .toList();
            List<Dish> dishesToKeep = new ArrayList<>(dishesFromFile);
            dishesToKeep.retainAll(dishRepository.findAll());
            dishRepository.clearAll();
            dishRepository.saveAll(dishesToKeep);
            if (dishesToKeep.isEmpty()) {
                Path filePath = Paths.get(dishesFileName);
                Files.writeString(filePath, "");
            }
        } catch (NullPointerException | IOException igmored) {}
    }

}
