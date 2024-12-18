package com.servPet.pgOrder.model;

import java.time.LocalDate;

public class AvailableSlot {
	private LocalDate date;
	private Integer timeSlot; // 0:早, 1:中, 2:晚

	public AvailableSlot() {
	}

	public AvailableSlot(LocalDate date, Integer timeSlot) {
		this.date = date;
		this.timeSlot = timeSlot;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getTimeSlot() {
		return timeSlot;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTimeSlot(Integer timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getTimeSlotName() {
		switch (timeSlot) {
		case 0:
			return "早";
		case 1:
			return "中";
		case 2:
			return "晚";
		default:
			return "未知";
		}
	}
}
