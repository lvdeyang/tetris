package com.sumavision.bvc.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suma.venus.resource.bo.CreateBundleRequest;
import com.suma.venus.resource.bo.CreateBundleResponse;
import com.suma.venus.resource.bo.DeleteBundleRequest;
import com.suma.venus.resource.bo.DeleteBundleResponse;
import com.suma.venus.resource.bo.UpdateBundleRequest;
import com.suma.venus.resource.bo.UpdateBundleResponse;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name="tetris-resource", configuration = FeignConfiguration.class)
//@FeignClient(name="tetris-resource")
//@RequestMapping(value="/tetris-resource/api")
public interface ResourceRemoteService {
	@RequestMapping(value ="/api/createBundle",method=RequestMethod.POST)
	public CreateBundleResponse createBundle(@RequestBody CreateBundleRequest createBundleRequest);
	//public  String createBundle(@RequestParam("createRequestString")String resourceRequestString);
	@RequestMapping(value ="/api/updateBundle",method=RequestMethod.POST)
	 public UpdateBundleResponse updateBundle(@RequestBody UpdateBundleRequest request);
	@RequestMapping(value ="/api/deleteBundle",method=RequestMethod.POST)
	public DeleteBundleResponse deleteBundle(@RequestBody DeleteBundleRequest request);		
}
