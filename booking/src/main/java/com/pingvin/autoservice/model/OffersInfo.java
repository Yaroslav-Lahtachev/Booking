package com.pingvin.autoservice.model;

import com.pingvin.autoservice.entity.Offer;

import java.util.ArrayList;
import java.util.List;

public class OffersInfo {
    private int id;
    private String name;
    private String prof;
    private double price;
    private int time;
    private String kitName;
    private List<String> offer = new ArrayList<>();
    private List<String> needKit = new ArrayList<>();

    public List<String> getOffer() {
        return offer;
    }

    public void setOffer(List<String> offer) {
        this.offer = offer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getNeedKit() {
        return needKit;
    }

    public void setNeedKit(List<String> needKit) {
        this.needKit = needKit;
    }

    public OffersInfo() {
    }

    public OffersInfo(String name, String prof, double price, int time) {
        this.name = name;
        this.prof = prof;
        this.price = price;
        this.time = time;
    }

    public OffersInfo(int id, String name, String prof, double price, int time, String kitName) {
        this.id = id;
        this.name = name;
        this.prof = prof;
        this.price = price;
        this.time = time;
        this.kitName = kitName;
    }

    public OffersInfo(Offer offer) {
        this.id = offer.getIdOffer();
        this.name = offer.getName();
        this.prof = offer.getProf();
        this.price = offer.getPrice();
        this.time = offer.getTime();
        this.kitName = offer.getKit().getName();
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName (String kitName) {
        this.kitName = kitName;
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
