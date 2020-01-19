package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum Jv220Param {

	H264("h264", 1l, 96l),
	H265("h265", 0l, 98l),
	
	G711A("pcma", 8l, 8l),
	G711U("pcmu", 0l, 0l),
	G729("g729", 18l, 18l),
	MPD("aac", 97l, 97l),
	
	P3840X2160("3840x2160", 71l, 0l),
	P1920X1200("1920x1200", 36l, 0l),
	P1920X1080("1920x1080", 18l, 0l),	
	P1280X720("1280x720", 9l, 0l),
	P720X576("720x576", 6l, 0l),
	P704X576("704x576", 42l, 0l),
	P352X288("352x288", 1l, 0l),
	
	//协议画面：0：解码1, 1：解码2, 2：解码3, 3：解码4, 4：解码5, 5：解码6, 6：解码7, 7：解码8, 8：编码1, 9：编码2
	VIDEOENCODE1("VenusVideoIn_1", 8l, 0l),
	VIDEOENCODE2("VenusVideoIn_2", 9l, 0l),
	VIDEODECODE1("VenusVideoOut_1", 0l, 0l),
	VIDEODECODE2("VenusVideoOut_2", 1l, 0l),
	VIDEODECODE3("VenusVideoOut_1", 2l, 0l),
	VIDEODECODE4("VenusVideoOut_2", 3l, 0l),
	VIDEODECODE5("VenusVideoOut_1", 4l, 0l),
	VIDEODECODE6("VenusVideoOut_2", 5l, 0l),
	VIDEODECODE7("VenusVideoOut_1", 6l, 0l),
	VIDEODECODE8("VenusVideoOut_2", 7l, 0l);
	
	private String name;
	
	private Long protocal;
	
	private Long relation;
	
	private Jv220Param(String name, Long protocal, Long relation){
		this.name = name;
		this.protocal = protocal;
		this.relation = relation;
	}

	public String getName() {
		return name;
	}

	public Long getProtocal() {
		return protocal;
	}

	public Long getRelation() {
		return relation;
	}

	public static Jv220Param fromName(String name) throws Exception{
		Jv220Param[] values = Jv220Param.values();
		for(Jv220Param value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
