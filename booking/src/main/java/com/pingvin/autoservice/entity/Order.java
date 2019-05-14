package com.pingvin.autoservice.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer", referencedColumnName = "id")
    private User customer;

    @OneToOne
    @JoinColumn(name = "offer", referencedColumnName = "id")
    private Offer offer;

    @Column(name = "dateStart", nullable = true)
    private Date dateStart;

    @Column(name = "dateFinish", nullable = true)
    private Date dateFinish;

    @JoinColumn(name = "master", referencedColumnName = "id")
    private Master masterId;

    @OneToOne
    @JoinColumn(name = "kit", referencedColumnName = "id")
    private Parts kitId;

    public int getIdOrder() {
        return id;
    }

    public void setIdOrder(int id) {
        this.id = id;
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
        return masterId;
    }

    public void setMaster(Master masterId) {
        this.masterId = masterId;
    }

    public Parts getKitId() {
        return kitId;
    }

    public void setKitId(Parts kitId) {
        this.kitId = kitId;
    }

    public Order() {
    }

    public Order(int id, User customer, Offer offer, Date dateStart, Date dateFinish, Master masterId, Parts kitId) {
        this.id = id;
        this.customer = customer;
        this.offer = offer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.masterId = masterId;
        this.kitId = kitId;
    }
}
