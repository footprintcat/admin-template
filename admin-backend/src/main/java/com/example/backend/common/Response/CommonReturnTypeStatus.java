package com.example.backend.common.Response;

public enum CommonReturnTypeStatus {
    SUCCESS("success"),
    FAILED("fail");

    private final String str;

    CommonReturnTypeStatus(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}