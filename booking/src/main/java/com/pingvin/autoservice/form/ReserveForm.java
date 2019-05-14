package com.pingvin.autoservice.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ReserveForm {
    private int idOffer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFinish;

    public ReserveForm() {
    }

    public ReserveForm(int idOffer, Date dateStart, Date dateFinish) {
        this.idOffer = idOffer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
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
        this.dateStart = dateStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }
}
