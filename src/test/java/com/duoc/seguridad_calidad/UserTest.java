package com.duoc.seguridad_calidad;


import org.junit.jupiter.api.Test;

import com.duoc.seguridad_calidad.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testConstructorWithParameters() {
        // Dado: Creamos una instancia de User usando el constructor con parámetros
        String username = "jdoe";
        String name = "John Doe";
        String email = "jdoe@example.com";
        String password = "password123";
        
        User user = new User(username, name, email, password);
        
        // Entonces: Verificamos que los valores se asignen correctamente
        assertEquals(username, user.getUsername());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void testDefaultConstructor() {
        // Dado: Creamos una instancia de User usando el constructor por defecto
        User user = new User();

        // Entonces: Verificamos que los valores por defecto son null
        assertNull(user.getUsername());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        // Dado: Creamos una instancia de User
        User user = new User();
        
        // Dado: Asignamos valores a través de los setters
        user.setUsername("jdoe");
        user.setName("John Doe");
        user.setEmail("jdoe@example.com");
        user.setPassword("password123");
        
        // Entonces: Verificamos que los valores asignados se obtienen correctamente
        assertEquals("jdoe", user.getUsername());
        assertEquals("John Doe", user.getName());
        assertEquals("jdoe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testRole() {
        // Dado: Creamos una instancia de User
        User user = new User();
        
        // Dado: Asignamos un rol
        user.setRole("ADMIN");
        
        // Entonces: Verificamos que el rol es el esperado
        assertEquals("ADMIN", user.getRole());
    }
}