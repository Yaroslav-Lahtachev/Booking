package com.pingvin.autoservice.model;

import com.pingvin.autoservice.entity.Offer;
import com.pingvin.autoservice.form.OfferForm;

public class OffersInfo {
    private int id;
    private String name;
    private String prof;
    private double price;
    private int time;

    public OffersInfo() {
    }

    public OffersInfo(String name, String prof, double price, int time) {
        this.name = name;
        this.prof = prof;
        this.price = price;
        this.time = time;
    }

    public OffersInfo( int id, String name, String prof, double price, int time) {
        this.id = id;
        this.name = name;
        this.prof = prof;
        this.price = price;
        this.time = time;
    }

    public OffersInfo(Offer offer) {
        this.id = offer.getIdOffer();
        this.name = offer.getName();
        this.prof = offer.getProf();
        this.price = offer.getPrice();
        this.time = offer.getTime();
    }

    public OffersInfo(OfferForm offerForm) {
        this.id = offerForm.getIdOffer();
        this.name = offerForm.getName();
        this.prof = offerForm.getProf();
        this.price = offerForm.getPrice();
        this.time = offerForm.getTime();
    }

    public int getIdOffer() {
        return id;
    }

    public void setIdOffer(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String city) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String address) {
        this.prof = prof;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OffersInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prof='" + prof + '\'' +
                ", time=" + time +
                ", price=" + price +
                '}';
    }
}
