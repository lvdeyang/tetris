package com.sumavision.tetris.capacity.bo.task;


public class AudioProcessBO {

    private Integer sample;
    private String codec;
    private Integer volume;
    private String ch_layout;
    private String denoise;
    private String audio_dup_mode;
    private Integer sample_fmt;
    private String agc_gain;

    public AudioProcessBO(Integer sample, String codec, Integer volume,
                          String ch_layout, String denoise, String audio_dup_mode,
                          Integer sample_fmt, String agc_gain) {
        this.sample = sample;
        this.codec = codec;
        this.volume = volume;
        this.ch_layout = ch_layout;
        this.denoise = denoise;
        this.audio_dup_mode = audio_dup_mode;
        this.sample_fmt = sample_fmt;
        this.agc_gain = agc_gain;
    }

    public Integer getSample() {
        return sample;
    }

    public AudioProcessBO setSample(Integer sample) {
        this.sample = sample;
        return this;
    }

    public String getCodec() {
        return codec;
    }

    public AudioProcessBO setCodec(String codec) {
        this.codec = codec;
        return this;
    }

    public Integer getVolume() {
        return volume;
    }

    public AudioProcessBO setVolume(Integer volume) {
        this.volume = volume;
        return this;
    }

    public String getCh_layout() {
        return ch_layout;
    }

    public AudioProcessBO setCh_layout(String ch_layout) {
        this.ch_layout = ch_layout;
        return this;
    }

    public String getDenoise() {
        return denoise;
    }

    public AudioProcessBO setDenoise(String denoise) {
        this.denoise = denoise;
        return this;
    }

    public String getAudio_dup_mode() {
        return audio_dup_mode;
    }

    public AudioProcessBO setAudio_dup_mode(String audio_dup_mode) {
        this.audio_dup_mode = audio_dup_mode;
        return this;
    }

    public Integer getSample_fmt() {
        return sample_fmt;
    }

    public AudioProcessBO setSample_fmt(Integer sample_fmt) {
        this.sample_fmt = sample_fmt;
        return this;
    }

    public String getAgc_gain() {
        return agc_gain;
    }

    public AudioProcessBO setAgc_gain(String agc_gain) {
        this.agc_gain = agc_gain;
        return this;
    }
}
