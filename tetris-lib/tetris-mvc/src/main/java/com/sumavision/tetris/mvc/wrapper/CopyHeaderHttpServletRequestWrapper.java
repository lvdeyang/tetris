package com.sumavision.tetris.mvc.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CopyHeaderHttpServletRequestWrapper extends HttpServletRequestWrapper{

	private Map<String, String> headerMap = new HashMap<>();
	
	private HttpServletRequest nativeRequest;
	
	public CopyHeaderHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.nativeRequest = request;
		final Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                if (!headerMap.containsKey(name)){
                	headerMap.put(name, values);
                }
            }
        }
	}
	 
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }
 
    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }
 
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }
	
}
