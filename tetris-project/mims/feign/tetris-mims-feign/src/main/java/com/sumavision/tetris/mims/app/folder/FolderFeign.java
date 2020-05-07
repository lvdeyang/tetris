package com.sumavision.tetris.mims.app.folder;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.config.feign.FeignConfiguration;

/**
 * 媒资仓库目录feign接口<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年4月29日 下午5:05:08
 */
@FeignClient(name = "tetris-mims", configuration = FeignConfiguration.class)
public interface FolderFeign {
	/**
	 * 删除媒资库文件夹<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午5:05:08
	 * @param Long folderId 待删除的文件夹id
	 */
	@RequestMapping(value = "/folder/feign/media/remove")
	public JSONObject delete(@RequestParam("folderId") Long folderId)throws Exception;
}
