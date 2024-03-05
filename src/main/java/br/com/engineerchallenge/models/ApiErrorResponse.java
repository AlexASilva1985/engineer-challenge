package br.com.engineerchallenge.models;

import java.util.List;

import lombok.Data;

@Data
public class ApiErrorResponse {

    private Integer code = null;
    private String message = null;
    private String description = null;
    private List<Error> errors = null;
}