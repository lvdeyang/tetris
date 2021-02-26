package com.sumavision.tetris.mims.app.media.stream.video.program;/**
 * Created by Poemafar on 2021/2/1 11:55
 */

/**
 * @ClassName: ResultCode
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/2/1 11:55
 */
public enum ResultCode {

    /**
     * 成功状态码
     */
    SUCCESS(0,"成功"),

    /**
     * 失败状态码
     */
    FAIL(1,"失败"),

    /**
     * 参数不合法
     */
    ILLEGAL_PARAMS(1001,"参数不合法");






    private Integer code;

    private String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public ResultCode setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultCode setMessage(String message) {
        this.message = message;
        return this;
    }


}
