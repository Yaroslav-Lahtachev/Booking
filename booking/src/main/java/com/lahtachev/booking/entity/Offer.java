package com.lahtachev.booking.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offers")
public class Offer implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "id_offer", referencedColumnName = "id_offer")
    private Seller idOffer;

    @Column(name = "city", nullable = false)
    private String city;


    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "max_people_count", nullable = false)
    private int maxPeopleCount;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "availability_parking", nullable = false)
    private boolean parking;

    @Column(name = "availability_wifi", nullable = false)
    private boolean wifi;

    @Column(name = "availability_animal", nullable = false)
    private boolean animal;

    @Column(name = "availability_smoking", nullable = false)
    private boolean smoking;

    public Seller getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(Seller idOffer) {
        this.idOffer = idOffer;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public int getMaxPeopleCount() {
        return maxPeopleCount;
    }

    public void setMaxPeopleCount(int maxPeopleCount) {
        this.maxPeopleCount = maxPeopleCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isAnimal() {
        return animal;
    }

    public void setAnimal(boolean animal) {
        this.animal = animal;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public Offer() {
    }

    public Offer(Seller idOffer, String city, String adress, int maxPeopleCount, double price, boolean parking, boolean wifi, boolean animal, boolean smoking) {
        this.idOffer = idOffer;
        this.city = city;
        this.address = adress;
        this.maxPeopleCount = maxPeopleCount;
        this.price = price;
        this.parking = parking;
        this.wifi = wifi;
        this.animal = animal;
        this.smoking = smoking;
    }
}


