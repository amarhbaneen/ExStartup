package com.example.StartupExercise.User;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"") // Escaping the table name for H2



public class User {
    @GeneratedValue
    @Id
    @Column(name = "id")

    private Integer id;

    private String username;
    @Column(name = "password" , nullable = false,length = 20)

    private String password;
    @Column(name = "firstname", nullable = false,length = 20)

    private String firstName;
    @Column(name = "surname", nullable = false,length = 20)

    private String surName;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
