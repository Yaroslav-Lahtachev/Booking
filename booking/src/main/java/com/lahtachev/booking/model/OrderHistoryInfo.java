package com.lahtachev.booking.model;

import com.lahtachev.booking.form.ReserveForm;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

public class OrderHistoryInfo {
    private int idOrder;
    private int idUser;
    private String loginBuyer;
    private int idOffer;
    private String loginSeller;
    private String address;
    private String city;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFinish;

    private int maxPeopleCount;
    private double price;
    private boolean parking;
    private boolean wifi;
    private boolean animal;
    private boolean smoking;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    private String status;


    public OrderHistoryInfo() {
    }

    public OrderHistoryInfo(Date dateStart, Date dateFinish) {
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    public OrderHistoryInfo(int idOrder, int idUser, int idOffer, Date dateStart, Date dateFinish, String status) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.idOffer = idOffer;
        this.dateStart = timeCut(dateStart);
        this.dateFinish = timeCut(dateFinish);
        this.status = status;
    }

    public OrderHistoryInfo(int idOrder, String loginSeller) {
        this.idOrder = idOrder;
        this.loginSeller = loginSeller;
    }

    public OrderHistoryInfo(int idOrder, int idOffer, String address, String status) {
        this.idOrder = idOrder;
        this.idOffer = idOffer;
        this.address = address;
        this.status = status;
    }

    public OrderHistoryInfo(int idOrder, int idUser, String loginBuyer, int idOffer, String loginSeller, String address, Date dateStart, Date dateFinish, String status) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.loginBuyer = loginBuyer;
        this.idOffer = idOffer;
        this.loginSeller = loginSeller;
        this.address = address;
        this.dateStart = timeCut(dateStart);
        this.dateFinish = timeCut(dateFinish);
        this.status = status;
    }

    public OrderHistoryInfo(int idOrder, int idOffer, int idUser, String loginSeller, String address, Date dateStart, Date dateFinish, String status) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.idOffer = idOffer;
        this.loginSeller = loginSeller;
        this.address = address;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.status = status;
    }

    public OrderHistoryInfo(int idOrder, int idOffer, String address, String city, int maxPeopleCount, double price, boolean parking, boolean wifi, boolean animal, boolean smoking, String status) {
        this.idOrder = idOrder;
        this.idOffer = idOffer;
        this.address = address;
        this.city = city;
        this.maxPeopleCount = maxPeopleCount;
        this.price = price;
        this.parking = parking;
        this.wifi = wifi;
        this.animal = animal;
        this.smoking = smoking;
        this.status = status;
    }

    public OrderHistoryInfo(int idOrder) {
        this.idOrder = idOrder;
    }

    public OrderHistoryInfo(ReserveForm reserveForm) {
        this.idOffer = reserveForm.getIdOffer();
        this.dateStart = reserveForm.getDateStart();
        this.dateFinish = reserveForm.getDateFinish();
    }

    public String getLoginSeller() {
        return loginSeller;
    }

    public void setLoginSeller(String loginSeller) {
        this.loginSeller = loginSeller;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoginBuyer() {
        return loginBuyer;
    }

    public void setLoginBuyer(String loginBuyer) {
        this.loginBuyer = loginBuyer;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = timeCut(dateStart);
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = timeCut(dateFinish);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderHistoryInfo{" +
                "idOrder=" + idOrder +
                ", idUser=" + idUser +
                ", loginBuyer='" + loginBuyer + '\'' +
                ", idOffer=" + idOffer +
                ", loginSeller='" + loginSeller + '\'' +
                ", address='" + address + '\'' +
                ", dateStart=" + dateStart +
                ", dateFinish=" + dateFinish +
                ", status='" + status + '\'' +
                '}';
    }

    public static Date timeCut(Date rawDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(rawDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
