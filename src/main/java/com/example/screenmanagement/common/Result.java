package com.example.screenmanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class Result {
    String message;
    boolean ok;
    String responseCode;

    public static Result OK(String message) {
        return new Result(message, true, "200");
    }
}
