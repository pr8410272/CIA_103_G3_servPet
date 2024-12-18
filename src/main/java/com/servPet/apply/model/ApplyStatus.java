package com.servPet.apply.model;

public enum ApplyStatus {
	PENDING("0"), APPROVED("1"), REJECTED("2");

    private final String code;

    ApplyStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
