package com.raredevz.eventivo.Helper;

import java.io.Serializable;

public class Booking implements Serializable {
    String venueId,venueName,venueCity,managerId,UserId,Date,Slot,RequiredCapacity,TRXID,payment;
    BookingStatus status;
    public Booking() {
    }

    @Override
    public String toString() {
        return
                "venueId: " + venueId + '\'' +
                ", venueName: '" + venueName + '\'' +
                ", venueCity: '" + venueCity + '\'' +
                ", managerId: '" + managerId + '\'' +
                ", UserId: '" + UserId + '\'' +
                ", Date: '" + Date + '\'' +
                ", Slot: '" + Slot + '\'' +
                ", RequiredCapacity: '" + RequiredCapacity + '\'' +
                ", TRXID: '" + TRXID + '\'' +
                ", Payment: '" + payment + '\'' +
                ", Status: " + status ;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getRequiredCapacity() {
        return RequiredCapacity;
    }

    public void setRequiredCapacity(String requiredCapacity) {
        RequiredCapacity = requiredCapacity;
    }

    public String getTRXID() {
        return TRXID;
    }

    public void setTRXID(String TRXID) {
        this.TRXID = TRXID;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
