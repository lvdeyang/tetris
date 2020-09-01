package com.sumavision.tetris.capacity.bo.input;/**
 * Created by Poemafar on 2020/8/24 16:27
 */

import java.io.Serializable;

/**
 * @ClassName: ModifyElementBO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/24 16:27
 */
public class ModifyElementBO implements Serializable {

    private static final long serialVersionUID = 2851873183878555228L;


    private String deinterlace_mode;

    private String backup_mode;

    private String pattern_path;

    private String decode_mode;

    private Integer cutoff_time;

    private Integer nv_card_idx;


    public String getDeinterlace_mode() {
        return deinterlace_mode;
    }

    public void setDeinterlace_mode(String deinterlace_mode) {
        this.deinterlace_mode = deinterlace_mode;
    }

    public String getBackup_mode() {
        return backup_mode;
    }

    public void setBackup_mode(String backup_mode) {
        this.backup_mode = backup_mode;
    }

    public String getPattern_path() {
        return pattern_path;
    }

    public void setPattern_path(String pattern_path) {
        this.pattern_path = pattern_path;
    }

    public String getDecode_mode() {
        return decode_mode;
    }

    public void setDecode_mode(String decode_mode) {
        this.decode_mode = decode_mode;
    }

    public Integer getCutoff_time() {
        return cutoff_time;
    }

    public void setCutoff_time(Integer cutoff_time) {
        this.cutoff_time = cutoff_time;
    }

    public Integer getNv_card_idx() {
        return nv_card_idx;
    }

    public void setNv_card_idx(Integer nv_card_idx) {
        this.nv_card_idx = nv_card_idx;
    }
}
