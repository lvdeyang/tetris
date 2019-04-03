package com.sumavision.tetris.cms.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.cms.article.ArticleDAO;
import com.sumavision.tetris.cms.article.ArticlePO;
import com.sumavision.tetris.cms.article.ArticleQuery;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionDAO;
import com.sumavision.tetris.cms.article.ArticleRegionPermissionPO;
import com.sumavision.tetris.cms.article.ArticleType;
import com.sumavision.tetris.cms.column.ColumnDAO;
import com.sumavision.tetris.cms.column.ColumnPO;
import com.sumavision.tetris.cms.region.RegionDAO;
import com.sumavision.tetris.cms.region.RegionPO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticleDAO;
import com.sumavision.tetris.cms.relation.ColumnRelationArticlePO;
import com.sumavision.tetris.cms.template.TemplateDAO;
import com.sumavision.tetris.cms.template.TemplateId;
import com.sumavision.tetris.cms.template.TemplatePO;
import com.sumavision.tetris.cms.template.TemplateQuery;
import com.sumavision.tetris.cms.template.TemplateType;
import com.sumavision.tetris.cms.template.TemplateVO;
import com.sumavision.tetris.commons.util.tar.TarUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiServerService {
	
	@Autowired
	private Path path;
	
	@Autowired
	private TemplateQuery templateQuery;
	
	@Autowired
	private ArticleDAO articleDao;
	
	@Autowired
	private ArticleRegionPermissionDAO articleRegionPermissionDao;
	
	@Autowired
	private TemplateDAO templateDao;
	
	@Autowired
	private ArticleQuery articleQuery;
	
	@Autowired
	private RegionDAO regionDao;
	
	@Autowired
	private ColumnDAO columnDao;
	
	@Autowired
	private ColumnRelationArticleDAO columnRelationArticleDao;
	
	@Autowired
	private MediaAudioService mediaAudioService;

	/**
	 * 添加文章<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月25日 上午9:02:13
	 * @param UserVO user 用户
	 * @param String name 文章名
	 * @param String remark 备注
	 * @return ArticlePO 文章数据
	 */
	public ArticlePO add(		
			List<String> columns,
			String name, 
			String type,
			String author,
			String publishTime,
			String thumbnail,
			String remark,
			Boolean command,
			List<JSONObject> contents,
			List<String> regions) throws Exception{
		
		ArticlePO article = new ArticlePO();
		article.setName(name);
		article.setAuthor(author);
		article.setPublishTime(publishTime);
		article.setThumbnail(thumbnail);
		article.setRemark(remark);
		article.setUpdateTime(new Date());
		article.setType(ArticleType.valueOf(type));
		
		String webappPath = path.webappPath();
		String separator = File.separator;
		
		String baseFolder = new StringBufferWrapper().append(webappPath)
													 .append("cms")
													 .append(separator)
													 .append("resource")
													 .append(separator)
													 .append("article")
													 .append(separator)
													 .append("yjgb")
													 .append(separator)
													 .append("internal")
													 .append(separator)
													 .toString();
		File folderFile = new File(baseFolder);
		if(!folderFile.exists()){
			folderFile.mkdirs();
		}
		
		String baseUrl = new StringBufferWrapper().append("cms/resource/article/yjgb/internal")
												  .append("/")
												  .toString();
		String fileName = new StringBufferWrapper().append(article.getUuid()).append(".html").toString();
		
		File html = new File(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		if(!html.exists()) html.createNewFile();
		
		article.setPreviewUrl(new StringBufferWrapper().append(baseUrl).append(fileName).toString());
		article.setStorePath(new StringBufferWrapper().append(baseFolder).append(fileName).toString());
		articleDao.save(article);
		
		//查询所有内置模板
		StringBufferWrapper allHtml = new StringBufferWrapper();
		List<JSONObject> moduleJsons = new ArrayList<JSONObject>();
		List<TemplatePO> templates = templateDao.findByTypeOrderBySerialAsc(TemplateType.INTERNAL.toString());
		for(JSONObject content: contents){
			String contentType = content.getString("type");
			String contentValue = content.getString("value");
			
			TemplatePO template = templateQuery.queryTemplateByTemplateId(templates, contentType);
			TemplateVO view_template = new TemplateVO().setWithHtmlAndJs(template);
			String contentHtml = template.getHtml();
			String contentJs = template.getJs();
			JSONObject jsJson = JSONObject.parseArray(contentJs, JSONObject.class).get(0);
			jsJson.put("value", contentValue);
			String jsKey = jsJson.getString("key");
			view_template.setJs(jsJson.toString());
			
			String newHtml = contentHtml.replace("${"+jsKey+"}", contentValue);
			allHtml.append(newHtml);
			
			JSONObject module = new JSONObject();
			module.put("id", "module_" + new Date().getTime());
			module.put("template", view_template);
			module.put("render", null);
			module.put("mousein", null);
			
			moduleJsons.add(module);
		}
		
		File file = new File(article.getStorePath());
		FileUtils.writeStringToFile(file, articleQuery.generateHtml(allHtml.toString(), "", ""));
		article.setModules(moduleJsons.toString());
		article.setUpdateTime(new Date());
		articleDao.save(article);		
		
		//绑定地区
		if(regions != null && regions.size() > 0){
			List<RegionPO> allRegions = regionDao.findByCodeIn(regions);
			List<ArticleRegionPermissionPO> articleRegionPermissions = new ArrayList<ArticleRegionPermissionPO>();
			for(RegionPO regionPO: allRegions){
				ArticleRegionPermissionPO bindRegion = new ArticleRegionPermissionPO();
				bindRegion.setArticleId(article.getId());
				bindRegion.setArticleName(article.getName());
				bindRegion.setRegionId(regionPO.getId());
				bindRegion.setRegionName(regionPO.getName());
				articleRegionPermissions.add(bindRegion);
			}
			articleRegionPermissionDao.save(articleRegionPermissions);
		}		
		
		//绑定栏目
		if(columns != null && columns.size() > 0){
			List<ColumnPO> allColumns = columnDao.findByCodeIn(columns);
			List<ColumnRelationArticlePO> relations = columnRelationArticleDao.findByColumnCodeIn(columns);
			List<ColumnRelationArticlePO> columnRelationArticles = new ArrayList<ColumnRelationArticlePO>();
			for(ColumnPO columnPO: allColumns){
				
				Long tag = 0l;
				
				List<ColumnRelationArticlePO> exsitRelations = queryRelationsByColumnCode(relations, columnPO.getCode());		
				if(exsitRelations != null && exsitRelations.size()>0){
					Collections.sort(exsitRelations, new ColumnRelationArticlePO.ArticleOrderComparator());
					tag = exsitRelations.get(0).getArticleOrder();
				}
				
				ColumnRelationArticlePO bindColumn = new ColumnRelationArticlePO();
				bindColumn.setArticleId(article.getId());
				bindColumn.setArticleName(article.getName());
				bindColumn.setCommand(command);
				bindColumn.setArticleOrder(++tag);
				bindColumn.setColumnId(columnPO.getId());
				bindColumn.setColumnName(columnPO.getName());
				bindColumn.setColumnCode(columnPO.getCode());
				columnRelationArticles.add(bindColumn);
			}
			columnRelationArticleDao.save(columnRelationArticles);
		}
		
		return article;
	}
	
	/**
	 * 根据栏目编号筛选关联<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 下午5:38:37
	 * @param relations 关联表
	 * @param code 栏目编号
	 * @return List<ColumnRelationArticlePO>
	 */
	public List<ColumnRelationArticlePO> queryRelationsByColumnCode(List<ColumnRelationArticlePO> relations, String code){
		List<ColumnRelationArticlePO> needRelations = new ArrayList<ColumnRelationArticlePO>();
		for(ColumnRelationArticlePO relation: relations){
			if(relation.getColumnCode().equals(code)){
				needRelations.add(relation);
			}
		}
		return needRelations;
	}
	
	/**
	 * 压缩文件解析<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月1日 下午8:59:07
	 * @param path
	 * @throws Exception
	 */
	public Object parseCompress(String path) throws Exception{
		
		UserVO user = new UserVO().setGroupId("4")
				  .setGroupName("数码视讯")
				  .setUuid("5")
				  .setNickname("lvdeyang");
		
		File file = new File(path);
		String parentFilePath = new File(file.getParent()).getParent();
		String separator = File.separator;
		
		//定义建文章所需参数
		String content = "";
		List<JSONObject> contents = new ArrayList<JSONObject>();
		String type = "";
		List<String> columns = new ArrayList<String>();
		String articleName = "";
		String author = "";
		String publishTime = "";
		String remark = "";
		List<String> regions = new ArrayList<String>();
		String columnCode = "";
		String template = "";
		
		File parseFile = new File(new StringBufferWrapper().append(parentFilePath).append(separator).append("compress").toString());
		
		if(!parseFile.exists()){
			parseFile.mkdir();
		}
		
		//解压到指定文件夹
		TarUtil.dearchive(file, parseFile);
		
		File[] fileList = parseFile.listFiles();
		for(File f: fileList){
			String fileName = f.getName();
			String name = fileName.split("\\.")[0];
			Long size = f.length();
			String uploadTempPath = f.getPath();
			
			String fileNameSuffix = f.getName().split("\\.")[1];
			if(fileNameSuffix.equals("mp3")){
				
				String folderType = "audio";
				String mimeType = "audio/mp3";
				
				MediaAudioVO audio = mediaAudioService.add(user, name, fileName, size, folderType, mimeType, uploadTempPath);
				
				template = "yjgb_audio"; 
				content = audio.getPreviewUrl();
				type = "AVIDEO";

			}else if(fileNameSuffix.equals("mp4")){
				
			}else if(fileNameSuffix.equals("txt")){
				
			}else if(fileNameSuffix.equals("jpg")){
				
			}else if(fileNameSuffix.equals("png")){
				
			}else if(fileNameSuffix.equals("gif")){
				
			}else if(fileNameSuffix.equals("xml")){
				
				//解析xml
				Map<String, String> xmlMap = parseXml(f.getPath());
				articleName = xmlMap.get("name");
				author = xmlMap.get("author");
				publishTime = xmlMap.get("publishTime");
				regions.add(xmlMap.get("region"));
				columnCode = xmlMap.get("column");
			}
		}
		
		//生成所需栏目code
		Map<String, String> codeMap = parseEventType(columnCode);
		columns.add(codeMap.get("code"));
		remark = codeMap.get("keyWords");
		
		//文章标题
		JSONObject json1 = new JSONObject();
		json1.put("type", "yjgb_title");
		json1.put("value", articleName);
		contents.add(json1);
		
		//文章作者
		JSONObject json2 = new JSONObject();
		json2.put("type", "yjgb_txt");
		json2.put("value", author);
		contents.add(json2);
		
		//文章内容
		JSONObject json3 = new JSONObject();
		json3.put("type", template);
		json3.put("value", content);
		contents.add(json2);
		
		//生成文章
		//add(columns, articleName, type, author, publishTime, null, remark, false, contents, regions);
		
		return null;
		
	}
	
	/**
	 * 解析xml返回需要字段<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月2日 下午7:25:03
	 * @param path xml路径
	 * @return Map<String, String> 字段map
	 */
	public Map<String, String> parseXml(String path) throws Exception{
		
		//创建一个DocumentBuilderFactory的对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        InputStream is = new FileInputStream(new File(path));
        
        String column = "";
        String name = "";
        String author = "";
        String publishTime = "";
        String region = "";
        
        //创建一个DocumentBuilder的对象
        try {
            //创建DocumentBuilder对象
            DocumentBuilder db = dbf.newDocumentBuilder();
            //通过DocumentBuilder对象的parser方法加载xml文件到当前项目下
            Document document = db.parse(is);
            //获取所有book节点的集合
            NodeList nodeList = document.getElementsByTagName("EBD");
            //通过nodelist的getLength()方法可以获取bookList的长度
            System.out.println("EBD一共有" + nodeList.getLength() + "个节点");

            Node EBD = nodeList.item(0);
            
            NodeList EBDList = EBD.getChildNodes();
            
            for(int i=0;i<EBDList.getLength();i++){
            	if(EBDList.item(i).getNodeName().equals("EBM")){
            		NodeList EBMList = EBDList.item(i).getChildNodes();
            		for(int j=0;j<EBMList.getLength();j++){
            			if(EBMList.item(j).getNodeName().equals("MsgBasicInfo")){
            				NodeList infoList = EBMList.item(j).getChildNodes();
            				for(int m=0;m<infoList.getLength();m++){
            					if(infoList.item(m).getNodeName().equals("SendTime")){
            						publishTime = infoList.item(m).getFirstChild().getNodeValue();
            						System.out.println(publishTime);          						
            					}
            					if(infoList.item(m).getNodeName().equals("EventType")){
            						column = infoList.item(m).getFirstChild().getNodeValue();
            						System.out.println(column);
            					}
            					if(infoList.item(m).getNodeName().equals("SenderName")){
            						author = infoList.item(m).getFirstChild().getNodeValue();
            						System.out.println(author);
            					}
            				}
            			}
            			if(EBMList.item(j).getNodeName().equals("MsgContent")){
            				NodeList contentList = EBMList.item(j).getChildNodes();
            				for(int m=0;m<contentList.getLength();m++){
            					if(contentList.item(m).getNodeName().equals("MsgTitle")){
            						name = contentList.item(m).getFirstChild().getNodeValue();
            						System.out.println(name);
            					}
//            					if(contentList.item(m).getNodeName().equals("MsgDesc")){
//            						remark = contentList.item(m).getFirstChild().getNodeValue();
//            						System.out.println(remark);
//            					}
            					if(contentList.item(m).getNodeName().equals("AreaCode")){
            						region = contentList.item(m).getFirstChild().getNodeValue();
            						System.out.println(region);
            					}
            				}
            			}
            		}
            	}
            }
            
        }catch(ParserConfigurationException e){
        	e.printStackTrace();
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
			is.close();
		} 
        
        return new HashMapWrapper<String, String>().put("name", name)
        										   .put("column", column)
        										   .put("author", author)
        										   .put("publishTime", publishTime)
        										   .put("region", region)
        										   .getMap();
	}
	
	/**
	 * 解析eventType.json<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param code 分类（栏目）编码
	 * @return code 所在栏目编码
	 * @return keyWords 关键字
	 */
	public Map<String, String> parseEventType(String code) throws Exception{
		
		String separator = "，";

		String needCode = "";
		StringBufferWrapper keyWordsBuffer = new StringBufferWrapper();
		
		File jsonFile = ResourceUtils.getFile("classpath:eventType.json");
		String json = FileUtils.readFileToString(jsonFile);
        List<JSONObject> firstArray = JSONArray.parseArray(json, JSONObject.class);
		if(firstArray != null && firstArray.size() > 0){
			for(JSONObject first: firstArray){
				if(first.getString("code").equals(code)){
					needCode = first.getString("code");
					keyWordsBuffer.append(first.getString("text"));
					break;
				}
				if(first.getString("children") != null){
					List<JSONObject> secondArray = JSONArray.parseArray(first.getString("children"), JSONObject.class);
					if(secondArray.size() > 0){
						for(JSONObject second: secondArray){
							if(second.getString("code").equals(code)){
								needCode = second.getString("code");
								keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text"));
								break;
							}
							if(second.getString("children") != null){
								List<JSONObject> thirdArray = JSONArray.parseArray(second.getString("children"), JSONObject.class);
								if(thirdArray.size() > 0){
									for(JSONObject third: thirdArray){
										if(third.getString("code").equals(code)){
											needCode = third.getString("code");
											keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text")).append(separator).append(third.getString("text"));
											break;
										}
										if(third.getString("children") != null){
											List<JSONObject> forthArray = JSONArray.parseArray(third.getString("children"), JSONObject.class);
											if(forthArray.size() > 0){
												for(JSONObject forth: forthArray){
													if(forth.getString("code").equals(code)){
														needCode = third.getString("code");
														keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text")).append(separator).append(third.getString("text")).append(separator).append(forth.getString("text"));
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println(needCode);
		System.out.println(keyWordsBuffer.toString());

		return new HashMapWrapper<String, String>().put("code", needCode)
								   				   .put("keyWords", keyWordsBuffer.toString())
						   						   .getMap();
	}
	
	public static void main(String[] agrs) throws Exception{
		
		
		String separator = "，";
		
		String code = "11000";
		String needCode = "";
		StringBufferWrapper keyWordsBuffer = new StringBufferWrapper();
		
		File jsonFile = ResourceUtils.getFile("classpath:eventType.json");
		String json = FileUtils.readFileToString(jsonFile);
        List<JSONObject> firstArray = JSONArray.parseArray(json, JSONObject.class);
		if(firstArray != null && firstArray.size() > 0){
			for(JSONObject first: firstArray){
				if(first.getString("code").equals(code)){
					needCode = first.getString("code");
					keyWordsBuffer.append(first.getString("text"));
					break;
				}
				if(first.getString("children") != null){
					List<JSONObject> secondArray = JSONArray.parseArray(first.getString("children"), JSONObject.class);
					if(secondArray.size() > 0){
						for(JSONObject second: secondArray){
							if(second.getString("code").equals(code)){
								needCode = second.getString("code");
								keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text"));
								break;
							}
							if(second.getString("children") != null){
								List<JSONObject> thirdArray = JSONArray.parseArray(second.getString("children"), JSONObject.class);
								if(thirdArray.size() > 0){
									for(JSONObject third: thirdArray){
										if(third.getString("code").equals(code)){
											needCode = third.getString("code");
											keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text")).append(separator).append(third.getString("text"));
											break;
										}
										if(third.getString("children") != null){
											List<JSONObject> forthArray = JSONArray.parseArray(third.getString("children"), JSONObject.class);
											if(forthArray.size() > 0){
												for(JSONObject forth: forthArray){
													if(forth.getString("code").equals(code)){
														needCode = third.getString("code");
														keyWordsBuffer.append(first.getString("text")).append(separator).append(second.getString("text")).append(separator).append(third.getString("text")).append(separator).append(forth.getString("text"));
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println(needCode);
		System.out.println(keyWordsBuffer.toString());
	}
}
