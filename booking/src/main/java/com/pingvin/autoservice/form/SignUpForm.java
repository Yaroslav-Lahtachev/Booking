package com.pingvin.autoservice.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SignUpForm {
    private int idOffer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFinish;
    private String status;
    private String reason;

    public SignUpForm() {
    }

    public SignUpForm(int idOffer, Date dateStart, Date dateFinish, int needKit, String reason) {
        this.idOffer = idOffer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.reason = reason;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
