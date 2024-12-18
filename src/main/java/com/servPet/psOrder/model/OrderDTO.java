package com.servPet.psOrder.model;

public class OrderDTO {
    private Integer petId;
    private Integer svcId;
    private Integer psId;
    private String appointmentDate;
    private String timeSlot;

    // Getters and Setters


    public OrderDTO() {
    }

    public OrderDTO(Integer petId, Integer svcId, Integer psId, String appointmentDate, String timeSlot) {
        this.petId = petId;
        this.svcId = svcId;
        this.psId = psId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Integer getSvcId() {
        return svcId;
    }

    public void setSvcId(Integer svcId) {
        this.svcId = svcId;
    }

    public Integer getPsId() {
        return psId;
    }

    public void setPsId(Integer psId) {
        this.psId = psId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}

