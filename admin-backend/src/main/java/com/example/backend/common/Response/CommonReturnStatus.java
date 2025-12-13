package com.example.backend.common.Response;

public enum CommonReturnStatus {
    SUCCESS("success"),
    FAILED("fail");

    private final String str;

    CommonReturnStatus(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
