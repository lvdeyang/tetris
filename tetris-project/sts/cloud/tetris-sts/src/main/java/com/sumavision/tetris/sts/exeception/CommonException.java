package com.sumavision.tetris.sts.exeception;


/**
 * Created by Lost on 2017/2/14.
 */
public class CommonException extends Exception{

    private String causeName;

    private String errorCode;

    public CommonException(String errorCode) {
        this.errorCode = errorCode;
    }

    public CommonException(String causeName , String errorCode) {
        this.causeName = causeName;
        this.errorCode = errorCode;
    }

    public CommonException(String causeName , String errorCode , String message , Throwable cause) {
        super(message, cause);
        this.causeName = causeName;
        this.errorCode = errorCode;
    }

    public CommonException(CommunicationException exception){
        super(exception.getMessage(), exception.getCause());
        this.errorCode = exception.getErrorCode();
        this.causeName = exception.getSocketAddress();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getCauseName() {
        return causeName;
    }

    public void setCauseName(String causeName) {
        this.causeName = causeName;
    }
}
