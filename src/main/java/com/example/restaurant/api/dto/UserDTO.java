package com.example.restaurant.api.dto;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {
    @Getter
    @Setter
    @Expose
    private String login;
    @Getter @Setter
    @Expose
    private String password;

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
