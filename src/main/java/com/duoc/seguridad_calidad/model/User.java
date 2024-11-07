package com.duoc.seguridad_calidad.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class User {
    
    private String username;
    private String name;
    private String email;
    private String password;

    
    public User() {}

    public User(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
