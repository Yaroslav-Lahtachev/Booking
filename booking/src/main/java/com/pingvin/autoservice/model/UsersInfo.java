package com.pingvin.autoservice.model;

import com.pingvin.autoservice.entity.User;

import java.util.Objects;

public class UsersInfo {
    private int idUser;
    private String login;
    private String password;
    private String email;
    private String role;

    public UsersInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersInfo usersInfo = (UsersInfo) o;
        return idUser == usersInfo.idUser &&
                login.equals(usersInfo.login) &&
                password.equals(usersInfo.password) &&
                email.equals(usersInfo.email) &&
                role.equals(usersInfo.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, login, password, email, role);
    }

    public UsersInfo(int idUser, String login, String password, String email, String role) {
        this.setIdUser(idUser);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole(role);
    }

    public UsersInfo(int idUser, String login, String password, String email) {
        this.setIdUser(idUser);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
        this.setRole("USER");
    }

    public UsersInfo(User user) {
        this.idUser = user.getIdUser();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        if (this.role.equals("ROLE_ADMIN"))
            return true;
        return false;
    }

    public boolean isUser() {

        if (this.role.equals("ROLE_USER")) {
            return true;
        }
        return false;
    }
}