package com.example.restaurant.api.repository;

import com.example.restaurant.api.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private List<User> usersList = new ArrayList<>();

    public boolean checkForLogin(User userForm) {
        return usersList.stream().anyMatch(person -> person.getLogin().equals(userForm.getLogin())
                && person.checkPassword(userForm.getPassword()));
    }
    public boolean checkForRegistration(User userForm) {
        if (usersList.isEmpty() && userForm.getLogin().equals("admin")) {
            return true;
        }
        if (userForm.getPassword().length() > 20) {
            return true;
        }
        return usersList.stream().anyMatch(person ->
                person.getLogin().equals(userForm.getLogin()) ||
                        person.checkPassword(userForm.getPassword()) ||
                        userForm.getLogin().equals("admin"));
    }
    public void addPerson(User user) {
        usersList.add(user);
    }

    public User findByNameAndPass(User userForm) {
        return usersList.stream()
                .filter(user -> user.getLogin().equals(userForm.getLogin()) && user.checkPassword((userForm.getPassword())))
                .findFirst()
                .orElse(null);
    }

    public List<User> findAll() {
        return usersList;
    }

    public void saveAll(List<User> users) {
        usersList.addAll(users);
    }
}
