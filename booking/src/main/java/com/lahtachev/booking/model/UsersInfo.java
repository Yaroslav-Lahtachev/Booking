package com.lahtachev.booking.model;

import com.lahtachev.booking.entity.User;

public class UsersInfo {
    private int idUser;
    private String login;
    private String password;
    private String status;

    public UsersInfo() {
    }

    public UsersInfo(int idUser, String login, String password, String status) {
        this.setIdUser(idUser);
        this.setLogin(login);
        this.setPassword(password);
        this.setStatus(status);
    }

    public UsersInfo(User user) {
        this.idUser = user.getIdUser();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.status = user.getStatus();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdmin() {
        if (this.status.equals("ROLE_ADMIN"))
            return true;
        return false;
    }

    public boolean isBlocked() {
        if (this.status.equals("ROLE_BLOCKED"))
            return true;
        return false;
    }

    public boolean isUser() {

        if (this.status.equals("ROLE_USER")) {
            return true;
        }
        return false;
    }

    public boolean isSeller() {

        if (this.status.equals("ROLE_SELLER")) {
            return true;
        }
        return false;
    }
}