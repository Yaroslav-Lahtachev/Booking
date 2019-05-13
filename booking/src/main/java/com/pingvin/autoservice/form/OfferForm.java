package com.pingvin.autoservice.form;

import com.pingvin.autoservice.model.OffersInfo;

public class OfferForm {
    private int idOffer;
    private String city;
    private String address;
    private int maxPeopleCount;
    private double price;
    private boolean parking;
    private boolean wifi;
    private boolean animal;
    private boolean smoking;

    private boolean newOffer = false;
    private int idSeller;


    public OfferForm() {
        this.newOffer = true;
    }

    public OfferForm(int idOffer, String city, String address, int maxPeopleCount, double price, boolean parking, boolean wifi, boolean animal, boolean smoking, int idSeller) {
        this.idOffer = idOffer;
        this.city = city;
        this.address = address;
        this.maxPeopleCount = maxPeopleCount;
        this.price = price;
        this.parking = parking;
        this.wifi = wifi;
        this.animal = animal;
        this.smoking = smoking;
        this.idSeller = idSeller;
    }

    public OfferForm(OffersInfo offersInfo) {
        this.idOffer = offersInfo.getIdOffer();
        this.city = offersInfo.getCity();
        this.address = offersInfo.getAddress();
        this.maxPeopleCount = offersInfo.getMaxPeopleCount();
        this.price = offersInfo.getPrice();
        this.parking = offersInfo.isParking();
        this.wifi = offersInfo.isWifi();
        this.animal = offersInfo.isAnimal();
        this.smoking = offersInfo.isSmoking();
        this.idSeller = offersInfo.getIdSeller();
    }


    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public boolean isNewOffer() {
        return newOffer;
    }

    public void setNewOffer(boolean newOffer) {
        this.newOffer = newOffer;
    }

    public int getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(int idSeller) {
        this.idSeller = idSeller;
    }
}
