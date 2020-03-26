package com.sumavision.tetris.record.file;

import org.springframework.web.client.RestTemplate;

public class TEST {

	public static void main(String[] args) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		String t = restTemplate.getForObject("http://10.10.40.116:8081/custom测试2/1585098180000/record.xml", String.class);
		
		System.out.println(t);
	}

}
