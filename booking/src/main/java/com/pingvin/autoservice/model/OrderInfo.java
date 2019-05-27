package com.pingvin.autoservice.model;

import com.pingvin.autoservice.entity.Order;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OrderInfo {
    private int id;
    private int customer;
    private int offer;
    private String nameOffer;
    private double price;
    private int masterId;
    private int needKit;
    private String status;
    private String masterName;
    private String customerName;
    private String kitName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFinish;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName (String kitName) {
        this.kitName = kitName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getNeedKit() {
        return needKit;
    }

    public void setNeedKit(int needKit) {
        this.needKit = needKit;
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

    public String getNameOffer() {
        return nameOffer;
    }

    public void setNameOffer(String nameOffer) {
        this.nameOffer = nameOffer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public OrderInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfo orderInfo = (OrderInfo) o;
        return id == orderInfo.id &&
                customer == orderInfo.customer &&
                offer == orderInfo.offer &&
                Double.compare(orderInfo.price, price) == 0 &&
                masterId == orderInfo.masterId &&
                needKit == orderInfo.needKit &&
                nameOffer.equals(orderInfo.nameOffer) &&
                status.equals(orderInfo.status) &&
                masterName.equals(orderInfo.masterName) &&
                customerName.equals(orderInfo.customerName) &&
                kitName.equals(orderInfo.kitName) &&
                dateStart.equals(orderInfo.dateStart) &&
                dateFinish.equals(orderInfo.dateFinish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, offer, nameOffer, price, masterId, needKit, status, masterName, customerName, kitName, dateStart, dateFinish);
    }

    public OrderInfo(Date dateStart, Date dateFinish) {
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    public OrderInfo(int offer, Date dateStart, Date dateFinish) {
        this.offer = offer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
    }

    public OrderInfo(int id, int customer, int offer, int masterId, int needKit, Date dateStart, Date dateFinish) {
        this.id = id;
        this.customer = customer;
        this.offer = offer;
        this.masterId = masterId;
        this.needKit = needKit;
        this.dateStart = timeCut(dateStart);
        this.dateFinish = timeCut(dateFinish);
    }

    public OrderInfo(int id, int offer, String nameOffer, double price, String masterName, Date dateStart, Date dateFinish, String status, int needKit, String kitName) {
        this.id = id;
        this.offer = offer;
        this.nameOffer = nameOffer;
        this.price = price;
        this.masterName = masterName;
        this.dateStart = timeCut(dateStart);
        this.dateFinish = timeCut(dateFinish);
        this.status = status;
        this.needKit = needKit;
        this.kitName = kitName;
    }

    public OrderInfo(int id, int offer, String nameOffer, double price, String masterName, Date dateStart, Date dateFinish, String status, int needKit) {
        this.id = id;
        this.offer = offer;
        this.nameOffer = nameOffer;
        this.price = price;
        this.masterName = masterName;
        this.dateStart = timeCut(dateStart);
        this.dateFinish = timeCut(dateFinish);
        this.status = status;
        this.needKit = needKit;
    }


    public OrderInfo(Order order) {
        this.id = order.getIdOrder();
        this.offer = order.getOffer().getIdOffer();
        this.nameOffer = order.getOffer().getName();
        this.price = order.getOffer().getPrice();
        this.masterName = order.getMaster().getName();
        this.dateStart = timeCut(order.getDateStart());
        this.dateFinish = timeCut(order.getDateFinish());
        this.customer = order.getCustomer().getIdUser();
        this.customerName = order.getCustomer().getLogin();
        this.status = order.getStatus();
        this.needKit = order.getNeedKit();
        this.kitName = order.getOffer().getKit().getName();
    }

    @Override
    public String toString() {
        return "OrderHistoryInfo{" +
                "id=" + id +
                ", customer=" + customer +
                ", offer='" + offer + '\'' +
                ", masterId=" + masterId +
                ", kitId='" + needKit + '\'' +
                ", dateStart=" + dateStart +
                ", dateFinish=" + dateFinish +
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
