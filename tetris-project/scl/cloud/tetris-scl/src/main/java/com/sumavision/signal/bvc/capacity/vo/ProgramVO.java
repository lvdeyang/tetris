package com.sumavision.signal.bvc.capacity.vo;/**
 * Created by Poemafar on 2020/11/4 14:03
 */

/**
 * @ClassName: ProgramVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/4 14:03
 */
public class ProgramVO {

    private Integer program_number;

    private String provider;

    private Integer pcr_pid;

    private String character_set;
    /**
     * 节目名
     */
    private String program_name;

    private Integer pmt_pid;


    public Integer getProgram_number() {
        return program_number;
    }

    public void setProgram_number(Integer program_number) {
        this.program_number = program_number;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getPcr_pid() {
        return pcr_pid;
    }

    public void setPcr_pid(Integer pcr_pid) {
        this.pcr_pid = pcr_pid;
    }

    public String getCharacter_set() {
        return character_set;
    }

    public void setCharacter_set(String character_set) {
        this.character_set = character_set;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public Integer getPmt_pid() {
        return pmt_pid;
    }

    public void setPmt_pid(Integer pmt_pid) {
        this.pmt_pid = pmt_pid;
    }


}
