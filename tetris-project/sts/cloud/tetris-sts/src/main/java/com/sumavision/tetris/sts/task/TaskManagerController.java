package com.sumavision.tetris.sts.task;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping(value = "/task")
public class TaskManagerController{
	
	@Autowired
	private TaskAnalysisService taskAnalysisService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping("/addTask")
	public Object addTask(@RequestBody JSONObject obj, HttpServletRequest request)  throws Exception{
		taskAnalysisService.analysisAddTask(obj);
		return null;
	}


}
