package com.sumavision.tetris.mims.app.media.compress;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface MediaCompressFeign {

	/**
	 * 根据媒资id查询媒资<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月1日 下午5:46:46
	 * @param Long id 媒资id
	 * @return MediaCompressVO 播发媒资
	 */
	@RequestMapping(value = "/media/compress/feign/query")
	public JSONObject query(@RequestParam("id") Long id) throws Exception;
	
	/**
	 * 加载文件夹下的播发媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午5:03:13
	 * @param folderId 文件夹id
	 * @return rows List<MediaCompressVO> 播发媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@RequestMapping(value = "/media/compress/feign/load")
	public JSONObject load(@RequestParam("folderId") Long folderId) throws Exception;
	
	/**
	 * 压缩播发媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param String jsonString 播发媒资json描述
	 * @param JSONArray mimsUuidList 打包媒资Uuid列表
	 * @return MediaCompressVO 生成的播发媒资
	 */
	//@RequestMapping(value = "/media/compress/feign/package/tar", method = RequestMethod.POST)
	//public JSONObject packageTar(@RequestParam("jsonString") String jsonString, @RequestParam("mimsUuidList") String mimsUuidList);

	@RequestMapping(value = "/media/compress/feign/package/tar", method = RequestMethod.POST)
	public JSONObject packageTar(@RequestBody String jsonString, @RequestParam("mimsUuidList") String mimsUuidList);

	
}
