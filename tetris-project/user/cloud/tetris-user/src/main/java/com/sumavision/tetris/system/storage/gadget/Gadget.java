package com.sumavision.tetris.system.storage.gadget;

import java.util.Collection;
import java.util.List;

/**
 * 小工具控制接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年11月6日 下午1:29:01
 */
public interface Gadget{

	/**
	 * 心跳<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月7日 下午1:31:11
	 * @param String baseControlPath 控制http根路径
	 * @return boolean 是否在线
	 */
	public boolean heartbeat(String baseControlPath);
	
	/**
	 * 获取存储能力信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午3:19:48
	 * @param String baseControlPath 控制http根路径
	 * @param String rootPath 存储根
	 * @return Storage 存储信息
	 */
	public Storage storageAbility(String baseControlPath, String rootPath) throws Exception;
	
	
	/**
	 * 获取cpu信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午3:08:51
	 * @param String baseControlPath 控制http根路径
	 * @return Cpu cpu信息
	 */
	public Cpu cpuPerformance(String baseControlPath) throws Exception;
	
	/**
	 * 获取指定目录占用空间大小--批量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月6日 下午2:57:16
	 * @param String baseControlPath 控制http根路径
	 * @param Collection<String> folderPathes 文件夹路径列表
	 * @return List<Directory> 目录信息
	 */
	public List<Directory> statisticsFolderSize(String baseControlPath, Collection<String> folderPathes) throws Exception;
	
}
