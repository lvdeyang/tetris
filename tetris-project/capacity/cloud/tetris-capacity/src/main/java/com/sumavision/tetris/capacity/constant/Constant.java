package com.sumavision.tetris.capacity.constant;/**
 * Created by Poemafar on 2021/1/11 16:25
 */

/**
 * @ClassName: Constant
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2021/1/11 16:25
 */
public class Constant {

    public static final Integer MAX_PORT = 65535;

    public static final Integer MIN_PORT = 1000;

    public static final Integer TRANSFORM_PORT = 5656;

    /**
     * 心跳间隔时长
     */
    public static final Integer HEARTBEAT_SPAN_SECOND = 5;

    private Constant() {
        throw new IllegalStateException("constant class");
    }
}
