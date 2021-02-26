package com.sumavision.tetris.mims.app.media.stream.video.program;/**
 * Created by Poemafar on 2021/1/29 15:32
 */

/**
 * @ClassName: ResultVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/1/29 15:32
 */
public class ResultVO {

    private Integer code;

    private String message;

    private String detail;

    private Object data;

    public ResultVO(){}

    public ResultVO(ResultCode resultCode){
        this.code=resultCode.getCode();
        this.message=resultCode.getMessage();
    }

    public ResultVO(ResultCode resultCode,Object data){
        this.code=resultCode.getCode();
        this.message=resultCode.getMessage();
        this.data=data;
    }

    public ResultVO setCodeAndMessage(ResultCode resultCode){
        this.code = resultCode.getCode();
        this.message=resultCode.getMessage();
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ResultVO setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResultVO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResultVO setData(Object data) {
        this.data = data;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public ResultVO setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
