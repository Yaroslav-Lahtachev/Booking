package com.pingvin.autoservice.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SignUpForm {
    private int idOffer;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateFinish;
    private boolean needKit;
    private String status;

    public SignUpForm() {
    }

    public SignUpForm(int idOffer, Date dateStart, Date dateFinish, boolean needKit) {
        this.idOffer = idOffer;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.needKit = needKit;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setNeedKit(boolean needKit) {
        this.needKit = needKit;
    }

    public boolean getNeedKit() {
        return needKit;
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
