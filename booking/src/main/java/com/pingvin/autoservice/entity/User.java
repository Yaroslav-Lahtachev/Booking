package com.pingvin.autoservice.entity;

import javax.persistence.*;

//TODO: why all our tables named as single entity? user, offer, order, should it be users, orders, offers?
@Entity
@Table(name = "user")
public class User {

    public static final String ROLE_BLOCKED = "BLOCKED";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "login", length = 45, unique = true, nullable = false)
    private String login;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "email", nullable = false)
    private String email;

    public int getIdUser() {
        return id;
    }

    public void setIdUser(int idUser) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFullName(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    public User(int id, String login, String password, String role, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
    }
}
