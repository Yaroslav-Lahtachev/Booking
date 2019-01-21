package com.lahtachev.booking.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_history")
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private int idOrder;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User idUser;

    @OneToOne
    @JoinColumn(name = "id_offer", referencedColumnName = "id_offer")
    private Seller idOffer;

    @Column(name = "date_start", nullable = true)
    private Date dateStart;

    @Column(name = "date_finish", nullable = true)
    private Date dateFinish;

    @Column(name = "status", nullable = false)
    private String status;

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Seller getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(Seller idOffer) {
        this.idOffer = idOffer;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderHistory() {
    }

    public OrderHistory(int idOrder, User idUser, Seller idOffer, Date dateStart, Date dateFinish, String status) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.idOffer = idOffer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.status = status;
    }

    public OrderHistory(Seller idOffer) {
        this.idOffer = idOffer;
    }
}
