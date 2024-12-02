package com.duoc.seguridad_calidad.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class User {
    
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String role;

    
    public User(int i, String string, String string2, String string3, String string4) {}

    public User(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User() {
        //TODO Auto-generated constructor stub
    }
}
