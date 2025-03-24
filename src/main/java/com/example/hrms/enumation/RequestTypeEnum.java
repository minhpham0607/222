package com.example.hrms.enumation;

import lombok.Getter;

@Getter
public enum RequestTypeEnum {
    PAID_LEAVE("Nghỉ phép có lương"),
    UNPAID_LEAVE("Nghỉ phép không lương");

    private final String value;

    RequestTypeEnum(String value) {
        this.value = value;
    }

}