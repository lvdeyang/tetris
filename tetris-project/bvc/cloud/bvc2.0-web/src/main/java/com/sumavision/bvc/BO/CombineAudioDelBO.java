package com.sumavision.bvc.BO;

import java.util.List;

import com.sumavision.bvc.BO.ForwardSetBO.DstBO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombineAudioDelBO {
	
	private List<DstBO> encode;

	@Override
	public String toString() {
		return "CombineAudioDelBO [encode=" + encode + "]";
	}	
}
