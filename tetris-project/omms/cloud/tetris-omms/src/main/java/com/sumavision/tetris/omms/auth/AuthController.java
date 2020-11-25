package com.sumavision.tetris.omms.auth;

import java.io.BufferedOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;


@Controller
@RequestMapping(value = "/auth")
public class AuthController {

	
	@Autowired
	private AuthDAO authDAO;
	
	@Autowired
	private AuthService authService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request)throws Exception{
		
		long total = authDAO.count();
		
		List<AuthVO> auths = authService.load(currentPage, pageSize);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", auths)
												   .getMap();
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String content,
			String deviceId,
			HttpServletRequest request) throws Exception{
		AuthPO entity = authService.add(name, content,deviceId);
		return new AuthVO().set(entity);
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String content,
			String deviceId,
			HttpServletRequest request) throws Exception{
		AuthPO entity = authService.edit(id, name,content,deviceId);
		return new AuthVO().set(entity);
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object edit(
			Long id,
			String content,
			HttpServletRequest request) throws Exception{
		AuthPO entity = authService.set(id,content);
		return new AuthVO().set(entity);
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		authService.delete(id);
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/importdev")
	public Object importdev(HttpServletRequest request) throws Exception{
		MultipartHttpServletRequestWrapper requestWrapper = new MultipartHttpServletRequestWrapper(request, -1);
		Long id = requestWrapper.getLong("id");
		FileItem importDevice = requestWrapper.getFileItem("deviceFile");
		AuthPO entity = authService.importDevice(id, importDevice);
		return new AuthVO().set(entity);
	}
	

    @RequestMapping(value = "/export/{id}")
	public ResponseEntity<byte[]> handleExport(@PathVariable Long id,HttpServletRequest request) throws Exception{
		AuthPO entity = authDAO.findOne(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", new MediaType(MediaType.TEXT_PLAIN, Charset.forName("utf-8")).toString());
        headers.add("Content-Disposition", "attchement;filename=auth("+entity.getName()+").csv");
        byte[] csvBytes = entity.getContent().getBytes();
        byte[] utf8Bytes = new byte[csvBytes.length+3];
        utf8Bytes[0] = (byte)0xEF;
        utf8Bytes[1] = (byte)0xBB;
        utf8Bytes[2] = (byte)0xBF;
        for(int i=0; i<csvBytes.length; i++){
        	utf8Bytes[i+3] = csvBytes[i];
        }
        return new ResponseEntity<byte[]>(utf8Bytes, headers, HttpStatus.OK);
	}
}
