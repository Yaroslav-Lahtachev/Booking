package com.lahtachev.booking.model;

import com.lahtachev.booking.entity.Offer;
import com.lahtachev.booking.form.OfferForm;

public class OffersInfo {
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


    public OffersInfo() {
    }

    public OffersInfo(String city, String address, int maxPeopleCount, double price, boolean parking, boolean wifi, boolean animal, boolean smoking, boolean newOffer) {
        this.city = city;
        this.address = address;
        this.maxPeopleCount = maxPeopleCount;
        this.price = price;
        this.parking = parking;
        this.wifi = wifi;
        this.animal = animal;
        this.smoking = smoking;
        this.newOffer = newOffer;
    }

    public OffersInfo(int idOffer, String city, String address, int maxPeopleCount, double price, boolean parking,
                      boolean wifi, boolean animal, boolean smoking) {
        this.idOffer = idOffer;
        this.city = city;
        this.address = address;
        this.maxPeopleCount = maxPeopleCount;
        this.price = price;
        this.parking = parking;
        this.wifi = wifi;
        this.animal = animal;
        this.smoking = smoking;
    }

    public OffersInfo(Offer offer) {
        this.idOffer = offer.getIdOffer().getIdOffer();
        this.city = offer.getCity();
        this.address = offer.getAdress();
        this.maxPeopleCount = offer.getMaxPeopleCount();
        this.price = offer.getPrice();
        this.parking = offer.isParking();
        this.wifi = offer.isWifi();
        this.animal = offer.isAnimal();
        this.smoking = offer.isSmoking();
    }

    public OffersInfo(OfferForm offerForm) {
        this.idOffer = offerForm.getIdOffer();
        this.city = offerForm.getCity();
        this.address = offerForm.getAddress();
        this.maxPeopleCount = offerForm.getMaxPeopleCount();
        this.price = offerForm.getPrice();
        this.parking = offerForm.isParking();
        this.wifi = offerForm.isWifi();
        this.animal = offerForm.isAnimal();
        this.smoking = offerForm.isSmoking();
        this.idSeller = offerForm.getIdSeller();
        this.newOffer = offerForm.isNewOffer();
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

    @Override
    public String toString() {
        return "OffersInfo{" +
                "idOffer=" + idOffer +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", maxPeopleCount=" + maxPeopleCount +
                ", price=" + price +
                ", parking=" + parking +
                ", wifi=" + wifi +
                ", animal=" + animal +
                ", smoking=" + smoking +
                ", newOffer=" + newOffer +
                '}';
    }

    public int getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(int idSeller) {
        this.idSeller = idSeller;
    }


}
