package com.pingvin.autoservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offers")
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "prof", nullable = false)
    private String prof;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "time", nullable = false)
    private int time;

    @OneToOne
    @JoinColumn(name = "kit", referencedColumnName = "id")
    private Parts kit;

    public int getIdOffer() {
        return id;
    }

    public void setIdOffer(int id) {
        this.id = id;
    }

    public Parts getKit() {
        return kit;
    }

    public void setKit(Parts kit) {
        this.kit = kit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Offer() {
    }

    public Offer(int id, String name, int price, String prof, int time, Parts kit) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.prof = prof;
        this.time = time;
        this.kit = kit;
    }
}


