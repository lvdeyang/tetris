package com.sumavision.tetris.record.file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.client.RestTemplate;

public class TEST {

	public static void main(String[] args) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		String t = restTemplate.getForObject("http://10.10.40.116:8081/custom测试2/1585098180000/record.xml", String.class);
		
		Document doc;
		try {
			doc = DocumentHelper.parseText(t);
			
			
			Element root = doc.getRootElement();

			for (Object obj : root.elements("folder")) {
				Element ele = (Element) obj;
				System.out.println(ele.toString());
				System.out.println(ele.getTextTrim());
				System.out.println(ele.getData());

			}			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
