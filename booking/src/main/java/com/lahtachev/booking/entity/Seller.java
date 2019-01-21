package com.lahtachev.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "seller", uniqueConstraints = {@UniqueConstraint(columnNames = "id_offer")})
public class Seller {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User idSeller;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offer", nullable = false)
    private int idOffer;

    public User getIdUser() {
        return idSeller;
    }

    public void setIdUser(User idUser) {
        this.idSeller = idUser;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int idOffer) {
        this.idOffer = idOffer;
    }

    public Seller() {
    }

    public Seller(User idUser) {
        this.idSeller = idUser;
    }
}
