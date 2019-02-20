package com.sumavision.tetris.cms.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;

@Controller
@RequestMapping(value = "/cms/resource")
public class ResourceController {

	@Autowired
	private Path path;
	
	/**
	 * 查询测试文本列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月20日 下午2:36:11
	 * @return List<TextVO> 测试文本列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/text")
	public Object listText(HttpServletRequest request) throws Exception{
		
		String webappPath = path.webappPath();
		
		String textFolder = new StringBufferWrapper().append("cms")
													 .append(File.separator)
													 .append("test")
													 .append(File.separator)
													 .append("text")
													 .toString();
		
		File file = new File(new StringBufferWrapper().append(webappPath).append(textFolder).toString());
		
		File[] texts = file.listFiles();
		if(texts!=null && texts.length>0){
			List<TextVO> view_texts = new ArrayList<TextVO>();
			for(int i=0; i<texts.length; i++){
				TextVO view_text = new TextVO().setName(texts[i].getName())
											   .setContent(FileUtils.readFileToString(texts[i]));
				view_texts.add(view_text);
			}
			
			return view_texts;
		}
		
		return null;
	}
	
	/**
	 * 查询测试图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月20日 下午2:36:53
	 * @return List<ImageVO> 测试图片列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/image")
	public Object listImage(HttpServletRequest request) throws Exception{
		
		String webappPath = path.webappPath();
		
		String imageFolder = new StringBufferWrapper().append("cms")
													  .append(File.separator)
													  .append("test")
													  .append(File.separator)
													  .append("image")
													  .toString();
		
		String previewFolder = "cms/test/image/";
		
		File file = new File(new StringBufferWrapper().append(webappPath).append(imageFolder).toString());
		
		File[] images = file.listFiles();
		if(images!=null && images.length>0){
			List<ImageVO> view_images = new ArrayList<ImageVO>();
			for(int i=0; i<images.length; i++){
				ImageVO view_image = new ImageVO().setName(images[i].getName())
												  .setPreviewUrl(new StringBufferWrapper().append(previewFolder).append(images[i].getName()).toString());
				view_images.add(view_image);
			}
			
			return view_images;
		}
		
		return null;
	}
	
}
