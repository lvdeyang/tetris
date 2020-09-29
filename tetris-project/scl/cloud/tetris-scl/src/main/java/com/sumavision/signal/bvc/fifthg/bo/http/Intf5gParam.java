package com.sumavision.signal.bvc.fifthg.bo.http;/**
 * Created by Poemafar on 2020/8/31 9:29
 */

import java.util.List;

/**
 * @ClassName: Intf5gParam
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 9:29
 */
public class Intf5gParam {

    private List<GBE> gbes;

    private S5g s5g;

    private Wifi wifi;

    public List<GBE> getGbes() {
        return gbes;
    }

    public void setGbes(List<GBE> gbes) {
        this.gbes = gbes;
    }

    public S5g getS5g() {
        return s5g;
    }

    public void setS5g(S5g s5g) {
        this.s5g = s5g;
    }

    public Wifi getWifi() {
        return wifi;
    }

    public void setWifi(Wifi wifi) {
        this.wifi = wifi;
    }
}
