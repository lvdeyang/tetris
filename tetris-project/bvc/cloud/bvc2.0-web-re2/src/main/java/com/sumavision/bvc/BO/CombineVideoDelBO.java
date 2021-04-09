package com.sumavision.bvc.BO;

import java.util.List;

import com.sumavision.bvc.BO.ForwardSetBO.DstBO;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombineVideoDelBO {
	
	private List<SrcBO> src;
	
	private List<DstBO> encode;

	@Override
	public String toString() {
		return "CombineVideoDelBO [src=" + src + ", encode=" + encode + "]";
	}
	
	

}
