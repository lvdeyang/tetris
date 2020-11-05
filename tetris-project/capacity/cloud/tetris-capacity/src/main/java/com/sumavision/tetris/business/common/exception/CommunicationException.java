package com.sumavision.tetris.business.common.exception;

/**
 * Created by Lost on 2017/2/7.
 */
public class CommunicationException extends Exception {

    private String errorCode;

    private String socketAddress;

    public CommunicationException(String errorCode) {
        this.errorCode = errorCode;
    }

    public CommunicationException(String socketAddress , String errorCode) {
        this.socketAddress = socketAddress;
        this.errorCode = errorCode;
    }

    public CommunicationException(String  socketAddress, String errorCode, String errMsg, Throwable cause) {
        super(errMsg , cause);
        this.socketAddress = socketAddress;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(String socketAddress) {
        this.socketAddress = socketAddress;
    }
}

