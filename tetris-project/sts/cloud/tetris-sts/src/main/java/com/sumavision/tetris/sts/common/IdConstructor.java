package com.sumavision.tetris.sts.common;

import java.util.concurrent.atomic.AtomicInteger;


public class IdConstructor {

    static final AtomicInteger MSG_ID = new AtomicInteger(1);

    public static synchronized Integer getMsgId(){
        return MSG_ID.getAndIncrement() > Integer.MAX_VALUE - 10 ? MSG_ID.getAndSet(1) : MSG_ID.get();
    }
}
