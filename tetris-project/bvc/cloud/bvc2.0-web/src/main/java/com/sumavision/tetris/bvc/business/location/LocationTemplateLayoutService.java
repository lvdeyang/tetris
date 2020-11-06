package com.sumavision.tetris.bvc.business.location;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.Lcase;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

@Service
public class LocationTemplateLayoutService {
	
	@Autowired
	private LocationTemplateLayoutDAO locationTemplateLayoutDao;
	
	@Autowired
	private LocationOfScreenWallDAO locationOfScreenWallDao;

	/**
	 * 添加屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午9:55:14
	 * @param templateName 模板名字
	 * @param screenNumberOfX x方向屏幕墙数量
	 * @param screenNumberOfY y方向屏幕墙数量
	 * @param userId 业务用户
	 */
	public LocationTemplateLayoutVO add(String templateName, 
			Integer screenNumberOfX, 
			Integer screenNumberOfY, 
			Long userId) throws Exception {
		
		LocationTemplateLayoutPO layout = new LocationTemplateLayoutPO()
//													.setUserId(userId)
													.setScreenNumberOfX(screenNumberOfX)
													.setScreenNumberOfY(screenNumberOfY)
													.setTemplateName(templateName);
	
		locationTemplateLayoutDao.save(layout);
		
		return new LocationTemplateLayoutVO().set(layout);
	}

	/**
	 * 删除屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:02:37
	 * @param id 屏幕墙模板id
	 */
	@Transactional
	public void delete(Long id) throws BaseException {
		
		String encoderBundleNameList= locationOfScreenWallDao.findByLocationTemplateLayoutId(id).stream().filter(screen->{
			if(LocationExecuteStatus.RUN.equals(screen.getStatus())){
				return true;
			}
			return false;
		}).map(LocationOfScreenWallPO::getEncoderBundleName).collect(Collectors.joining(","));
		
		if(encoderBundleNameList != null && !"".equals(encoderBundleNameList)){
			throw new BaseException(StatusCode.FORBIDDEN, "请先解绑其屏幕墙 "+encoderBundleNameList+" 编码器");
		}
		
		locationOfScreenWallDao.deleteByLocationTemplateLayoutId(id);
		
		locationTemplateLayoutDao.delete(id);
		
	}

	/**
	 * 修改屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:11:58
	 * @param id
	 * @param templateName
	 * @param screenNumberOfX
	 * @param screenNumberOfY
	 * @param userId
	 */
	@Transactional
	public LocationTemplateLayoutPO edit(Long id, 
			String templateName, 
			Integer screenNumberOfX, 
			Integer screenNumberOfY) throws Exception {
		
		LocationTemplateLayoutPO layout = locationTemplateLayoutDao.findOne(id);
		
		if(layout == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到模板");
		}
		
		String encoderBundleNameList= locationOfScreenWallDao.findByLocationTemplateLayoutId(id).stream().filter(screen->{
			if(LocationExecuteStatus.RUN.equals(screen.getStatus())){
				return true;
			}
			return false;
		}).map(LocationOfScreenWallPO::getEncoderBundleName).collect(Collectors.joining(","));
		
		if(encoderBundleNameList != null && !"".equals(encoderBundleNameList)){
			throw new BaseException(StatusCode.FORBIDDEN, "请先解绑其屏幕墙 "+encoderBundleNameList+" 编码器");
		}
		
		//处理多余的解码器绑定
		if(layout.getScreenNumberOfX()>screenNumberOfX || layout.getScreenNumberOfY()>screenNumberOfY){
			if(layout.getScreenNumberOfX()>screenNumberOfX && layout.getScreenNumberOfY()>screenNumberOfY){
				locationOfScreenWallDao.deleteByLocationTemplateLayoutIdAndLocationXAfterOrLocationTemplateLayoutIdAndLocationYAfter(id, screenNumberOfX, id, screenNumberOfX);
			}else if(layout.getScreenNumberOfX()>screenNumberOfX){
				locationOfScreenWallDao.deleteByLocationTemplateLayoutIdAndLocationXAfterOrLocationTemplateLayoutIdAndLocationYAfter(id, screenNumberOfX, id, layout.getScreenNumberOfY());
			}else {
				locationOfScreenWallDao.deleteByLocationTemplateLayoutIdAndLocationXAfterOrLocationTemplateLayoutIdAndLocationYAfter(id, layout.getScreenNumberOfX(), id, screenNumberOfY);
			}
		}
		
		layout.setScreenNumberOfX(screenNumberOfX)
			  .setScreenNumberOfY(screenNumberOfY)
			  .setTemplateName(templateName);
		
		locationTemplateLayoutDao.save(layout);
		
		return layout;
		
	}

	/**
	 * 查询屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:40:26
	 * @return List<LocationTemplateLayoutPO>
	 */
	public List<LocationTemplateLayoutVO> queryAll() {
		
		return locationTemplateLayoutDao.findAll().stream().map(new LocationTemplateLayoutVO()::set).collect(Collectors.toList());
	}
	
	/**
	 * 查询屏幕墙模板<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午10:40:26
	 * @param templateName 屏幕墙模板名称
	 * @return List<LocationTemplateLayoutPO>
	 */
//	public List<LocationTemplateLayoutVO> query(String templateName, Long userId) {
//		
//		return locationTemplateLayoutDao.findByTemplateNameContainingAndUserId(templateName, userId).stream().map(layout->{
//			try {
//				return new LocationTemplateLayoutVO().set(layout);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}).collect(Collectors.toList());
//	}
	
}
