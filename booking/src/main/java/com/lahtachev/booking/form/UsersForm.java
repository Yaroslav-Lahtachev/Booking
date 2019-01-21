package com.lahtachev.booking.form;

public class UsersForm {

    private int idUser;
    private String login;
    private String password;
    private String confirmPassword;
    private int status;

    public UsersForm() {

    }

    public UsersForm(String login, String password, String confirmPassword, int idUser, int status) {
        this.login = login;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.idUser = idUser;
        this.status = status;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
