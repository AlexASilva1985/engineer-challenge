package br.com.engineerchallenge.contants;

public enum ErrorCode {
    ERR_01("ERR-01"),
    ERR_02("ERR-02"),
    ERR_03("ERR-03"),
    ERR_04("ERR-04");

    private final String description;

    ErrorCode(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

