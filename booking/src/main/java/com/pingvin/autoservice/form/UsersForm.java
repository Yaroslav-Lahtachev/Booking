package com.pingvin.autoservice.form;

public class UsersForm {

    private int idUser;
    private String login;
    private String password;
    private String confirmPassword;
    private String email;

    public UsersForm() {

    }

    public UsersForm(String login, String password, String confirmPassword, int idUser, String email) {
        this.login = login;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.idUser = idUser;
        this.email = email;
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

}
