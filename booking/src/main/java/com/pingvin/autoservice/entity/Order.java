package com.pingvin.autoservice.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "offer", referencedColumnName = "id")
    private Offer offer;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateStart", nullable = true)
    private Date dateStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "dateFinish", nullable = true)
    private Date dateFinish;

    @ManyToOne
    @JoinColumn(name = "master", referencedColumnName = "id")
    private Master master;

    @Column(name = "needKit", nullable = false)
    private boolean needKit;

    @Column(name = "status", nullable = false)
    private String status;

    public int getIdOrder() {
        return id;
    }

    public void setIdOrder(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public boolean getNeedKit() {
        return needKit;
    }

    public void setNeedKit(boolean needKit) {
        this.needKit = needKit;
    }

    public Order() {
    }

    public Order(int id, User customer, Offer offer, Date dateStart, Date dateFinish, Master master, boolean needKit, String status) {
        this.id = id;
        this.customer = customer;
        this.offer = offer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.master = master;
        this.needKit = needKit;
        this.status = status;
    }
}
