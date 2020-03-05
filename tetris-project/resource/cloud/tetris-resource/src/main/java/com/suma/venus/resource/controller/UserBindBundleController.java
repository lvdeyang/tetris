package com.suma.venus.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.vo.UserBindBundleVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/user/bind/bundle")
public class UserBindBundleController {
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;

	/**
	 * 编码器查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月8日 下午1:59:32
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/encoders")
	public Object queryEncoders(
			String ip,
			String bundleName,
			String bundleId,
			String username,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<BundlePO> bundles = bundleDao.findBySearch(new StringBufferWrapper().append("%").append(bundleName).append("%").toString(), 
														new StringBufferWrapper().append("%").append(bundleId).append("%").toString(),
														new StringBufferWrapper().append("%").append(username).append("%").toString(),
														new StringBufferWrapper().append("%").append(ip).append("%").toString(),
														"jv210", "VenusVideoIn", page);
		return new HashMapWrapper<String, Object>().put("rows", bundles.getContent())
												   .put("total", bundles.getTotalElements())
												   .getMap();
	}
	
	/**
	 * 解码器查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月8日 下午1:59:32
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/decoders")
	public Object queryDecoders(
			String ip,
			String bundleName,
			String bundleId,
			String username,
			Integer currentPage,
			Integer pageSize,
			HttpServletRequest request) throws Exception{
		
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<BundlePO> bundles = bundleDao.findBySearch(new StringBufferWrapper().append("%").append(bundleName).append("%").toString(), 
														new StringBufferWrapper().append("%").append(bundleId).append("%").toString(),
														new StringBufferWrapper().append("%").append(username).append("%").toString(),
														new StringBufferWrapper().append("%").append(ip).append("%").toString(),
														"jv210", "VenusVideoOut", page);
		return new HashMapWrapper<String, Object>().put("rows", bundles.getContent())
												   .put("total", bundles.getTotalElements())
												   .getMap();
	}
	
	/**
	 * 绑定编解码器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月8日 下午2:55:11
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/bind")
	public Object bindEncoder(
			Long userId,
			String userName,
			Long encodeId,
			String encodeBundleId,
			String encodeBundleName,
			String encodeDeviceModel,
			Long decodeId,
			String decodeBundleId,
			String decodeBundleName,
			String decodeDeviceModel,
			HttpServletRequest request) throws Exception{
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserId(userId);
		if(map == null){
			map = new EncoderDecoderUserMap();
			map.setUserId(userId);
			map.setUserName(userName);
		}
		map.setDecodeBundleId(decodeBundleId);
		map.setDecodeBundleName(decodeBundleName);
		map.setDecodeId(decodeId);
		map.setDecodeDeviceModel(decodeDeviceModel);
		map.setEncodeBundleId(encodeBundleId);
		map.setEncodeId(encodeId);
		map.setEncodeBundleName(encodeBundleName);
		map.setEncodeDeviceModel(encodeDeviceModel);
		
		encoderDecoderUserMapDao.save(map);
		
		return null;
	}
	
	/**
	 * 查询绑定<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月8日 下午2:55:40
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object query(
			Long userId,
			HttpServletRequest request) throws Exception{
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserId(userId);
		UserBindBundleVO bind = new UserBindBundleVO();
		if(map != null){
			bind.setUserId(userId)
		    	.setUserName(map.getUserName());
			
			if(map.getEncodeId() != null){
				BundlePO encoder = bundleDao.findOne(map.getEncodeId());
				bind.setEncodeId(map.getEncodeId())
					.setEncodeBundleId(map.getEncodeBundleId())
			    	.setEncodeBundleName(map.getEncodeBundleName())
			    	.setEncodeDeviceModel(map.getEncodeDeviceModel())
			    	.setEncodeIp(encoder.getDeviceIp())
			    	.setEncodeUserName(encoder.getUsername());
			}
			if(map.getDecodeId() != null){
				BundlePO decoder = bundleDao.findOne(map.getDecodeId());
			    bind.setDecodeId(map.getDecodeId())
			    	.setDecodeBundleId(map.getDecodeBundleId())
			    	.setDecodeBundleName(map.getDecodeBundleName())
			    	.setDecodeDeviceModel(map.getDecodeDeviceModel())
			    	.setDecodeIp(decoder.getDeviceIp())
			    	.setDecodeUserName(decoder.getUsername());
			}
		}

		return bind;
	}
	
}
