package com.sumavision.tetris.cms.api;

public class SignatureResponse {
    private String message;
    private String sign;

    public String getMessage() {
        return message;
    }

    public String getSign() {
        return sign;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
