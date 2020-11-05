package com.sumavision.tetris.bvc.business.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandSystemQueryService {

	@Autowired
	private QueryFeign queryFeign;
	
	public Long queryCountOfTransmit() throws Exception{
		
		return queryFeign.queryCountOfTransmit().getLong("data");
		
	}
	
	public Long queryCountOfReview() throws Exception{
		
		return queryFeign.queryCountOfReview().getLong("data");
		
	}
}
