package com.pingvin.autoservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "masters")
public class Master implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "prof", nullable = false)
    private String prof;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "occupied", nullable = false)
    private String occupied;

    public int getMaster() {
        return id;
    }

    public void setMaster(int id) {
        this.id = id;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // TODO: WHY char(255) in database, should it be boolean?
    public String isOccupied() {
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
    }

    public Master() {
    }

    public Master(int id, String prof, String name, String occupied) {
        this.id = id;
        this.prof = prof;
        this.name = name;
        this.occupied = occupied;
    }
}


