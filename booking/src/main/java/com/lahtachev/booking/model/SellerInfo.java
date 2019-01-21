package com.lahtachev.booking.model;

import com.lahtachev.booking.entity.User;

public class SellerInfo {
    private User idUser;
    private int idOffer;

    public SellerInfo() {
    }

    public SellerInfo(User idUser, int idOffer) {
        this.idUser = idUser;
        this.idOffer = idOffer;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }
}
