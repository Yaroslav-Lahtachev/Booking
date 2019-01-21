package com.lahtachev.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    public static final String ROLE_BLOCKED = "BLOCKED";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_SELLER = "SELLER";
    public static final String ROLE_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    private int idUser;

    @Column(name = "login", length = 45, unique = true, nullable = false)
    private String login;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "status", nullable = false)
    private String status;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User() {
    }

    public User(int idUser, String login, String password, String status) {
        this.idUser = idUser;
        this.login = login;
        this.password = password;
        this.status = status;
    }
}
