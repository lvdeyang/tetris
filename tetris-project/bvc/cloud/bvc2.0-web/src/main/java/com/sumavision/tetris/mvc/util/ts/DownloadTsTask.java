package com.sumavision.tetris.mvc.util.ts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.sumavision.bvc.listener.MQListener.Path;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.util.ts.exception.DownloadM3u8FailException;
import com.sumavision.tetris.mvc.util.ts.exception.DownloadTsFailException;

/**
 * ts下载任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月18日 下午1:28:35
 */
public class DownloadTsTask {
	
	/** ts 下载临时目录 */
	private static final String TS_DOWNLOAD_TMP_FOLDER = "ts_download_tmp";

	/** 文件开始录制时间 */
	private Date fileStartTime;
	
	/** 下载文件时间下限 */
	private Date timeScopeLowerLimit;
	
	/** 下载文件时间上限 */
	private Date timeScopeUpperLimit;
	
	/** 任务id，格式：userId_timestamp */
	private String id;

	/** 操作用户 */
	private Long userId;
	
	/** 服务器ip */
	private String ip;
	
	/** 服务器端口 */
	private String port;
	
	/** context path */
	private String contextPath;
	
	/** http://ip:port/contextPath */
	private String basehttpPath;
	
	/** \\webappPath\\userId\\id\\ */
	private String baseStorePath;
	
	/** m3u8 http地址 */
	private String m3u8HttpPath;
	
	/** m3u8文件名 */
	private String m3u8Name;
	
	/** m3u8 本地存储路径 */
	private String m3u8StorePath;
	
	/** m3u8 是否下载完成 */
	private boolean m3u8Downloaded;
	
	/** m3u8是否解析完成 */
	private boolean m3u8Parsed;
	
	/** 是否合并 */
	private boolean merge;

	/** ts文件列表 */
	private List<TsFile> tsFiles;
	
	private String mergedTsName;
	
	/** 合并后的ts文件http地址，除ip端口外的url部分 */
	private String mergedTsLocalHttpPath;

	/** 合并后的ts文件存储路径 */
	private String mergedTsStorePath;
	
	/** 下载就绪 */
	private Callback onReady;
	
	/** 下载失败 */
	private Callback onError;
	
	/** http客户端 */
	private CloseableHttpClient httpClient;
	
	/** http 配置参数 */
	private RequestConfig httpConfig;
	
	public Date getFileStartTime() {
		return fileStartTime;
	}

	public DownloadTsTask setFileStartTime(Date fileStartTime) {
		this.fileStartTime = fileStartTime;
		return this;
	}

	public Date getTimeScopeUpperLimit() {
		return timeScopeUpperLimit;
	}

	public DownloadTsTask setTimeScopeUpperLimit(Date timeScopeUpperLimit) {
		this.timeScopeUpperLimit = timeScopeUpperLimit;
		return this;
	}

	public Date getTimeScopeLowerLimit() {
		return timeScopeLowerLimit;
	}

	public DownloadTsTask setTimeScopeLowerLimit(Date timeScopeLowerLimit) {
		this.timeScopeLowerLimit = timeScopeLowerLimit;
		return this;
	}

	public DownloadTsTask setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
		return this;
	}

	public DownloadTsTask setHttpConfig(RequestConfig httpConfig) {
		this.httpConfig = httpConfig;
		return this;
	}

	public String getId() {
		return id;
	}

	public DownloadTsTask setId(String id) {
		this.id = id;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public DownloadTsTask setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public DownloadTsTask setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getPort() {
		return port;
	}

	public DownloadTsTask setPort(String port) {
		this.port = port;
		return this;
	}

	public String getContextPath() {
		return contextPath;
	}

	public DownloadTsTask setContextPath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	public String getBasehttpPath() {
		return basehttpPath;
	}

	public DownloadTsTask setBasehttpPath(String basehttpPath) {
		this.basehttpPath = basehttpPath;
		return this;
	}

	public String getBaseStorePath() {
		return baseStorePath;
	}

	public DownloadTsTask setBaseStorePath(String baseStorePath) {
		this.baseStorePath = baseStorePath;
		return this;
	}

	public String getM3u8HttpPath() {
		return m3u8HttpPath;
	}

	public DownloadTsTask setM3u8HttpPath(String m3u8HttpPath) {
		this.m3u8HttpPath = m3u8HttpPath;
		return this;
	}
	
	public String getM3u8Name() {
		return m3u8Name;
	}

	public DownloadTsTask setM3u8Name(String m3u8Name) {
		this.m3u8Name = m3u8Name;
		return this;
	}

	public String getM3u8StorePath() {
		return m3u8StorePath;
	}

	public DownloadTsTask setM3u8StorePath(String m3u8StorePath) {
		this.m3u8StorePath = m3u8StorePath;
		return this;
	}

	public boolean isM3u8Downloaded() {
		return m3u8Downloaded;
	}

	public DownloadTsTask setM3u8Downloaded(boolean m3u8Downloaded) {
		this.m3u8Downloaded = m3u8Downloaded;
		return this;
	}

	public boolean isM3u8Parsed() {
		return m3u8Parsed;
	}

	public DownloadTsTask setM3u8Parsed(boolean m3u8Parsed) {
		this.m3u8Parsed = m3u8Parsed;
		return this;
	}

	public boolean isMerge() {
		return merge;
	}

	public DownloadTsTask setMerge(boolean merge) {
		this.merge = merge;
		return this;
	}

	public List<TsFile> getTsFiles() {
		return tsFiles;
	}

	public DownloadTsTask setTsFiles(List<TsFile> tsFiles) {
		this.tsFiles = tsFiles;
		return this;
	}

	public String getMergedTsName() {
		return mergedTsName;
	}

	public DownloadTsTask setMergedTsName(String mergedTsName) {
		this.mergedTsName = mergedTsName;
		return this;
	}

	public String getMergedTsLocalHttpPath() {
		return mergedTsLocalHttpPath;
	}

	public DownloadTsTask setMergedTsLocalHttpPath(String mergedTsLocalHttpPath) {
		this.mergedTsLocalHttpPath = mergedTsLocalHttpPath;
		return this;
	}

	public String getMergedTsStorePath() {
		return mergedTsStorePath;
	}

	public DownloadTsTask setMergedTsStorePath(String mergedTsStorePath) {
		this.mergedTsStorePath = mergedTsStorePath;
		return this;
	}

	public Callback getOnReady() {
		return onReady;
	}

	public DownloadTsTask setOnReady(Callback onReady) {
		this.onReady = onReady;
		return this;
	}

	public Callback getOnError() {
		return onError;
	}

	public DownloadTsTask setOnError(Callback onError) {
		this.onError = onError;
		return this;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public RequestConfig getHttpConfig() {
		return httpConfig;
	}

	/**
	 * ts文件描述<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午1:09:40
	 */
	public static class TsFile{
		
		/** 文件名 */
		private String name;
		
		/** http地址 */
		private String httpPath;
		
		/** 本服务存储路径 */
		private String storePath;
		
		/** 文件大小 */
		private String size;
		
		/** 是否已经下载成功 */
		private boolean downloaded;
		
		/** 文件开始时间 */
		private Date startTime;
		
		/** 文件结束时间 */
		private Date endTime;

		public String getName() {
			return name;
		}

		public TsFile setName(String name) {
			this.name = name;
			return this;
		}

		public String getHttpPath() {
			return httpPath;
		}

		public TsFile setHttpPath(String httpPath) {
			this.httpPath = httpPath;
			return this;
		}

		public String getStorePath() {
			return storePath;
		}

		public TsFile setStorePath(String storePath) {
			this.storePath = storePath;
			return this;
		}

		public String getSize() {
			return size;
		}

		public TsFile setSize(String size) {
			this.size = size;
			return this;
		}

		public boolean isDownloaded() {
			return downloaded;
		}

		public TsFile setDownloaded(boolean downloaded) {
			this.downloaded = downloaded;
			return this;
		}

		public Date getStartTime() {
			return startTime;
		}

		public TsFile setStartTime(Date startTime) {
			this.startTime = startTime;
			return this;
		}

		public Date getEndTime() {
			return endTime;
		}

		public TsFile setEndTime(Date endTime) {
			this.endTime = endTime;
			return this;
		}
		
	}
	
	/**
	 * 异步回调<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午5:35:42
	 */
	public static interface Callback{
		public void notify(DownloadTsTask task, Object attach);
	}
	
	/**
	 * 下载任务对列<br/>
	 * <p>
	 * 	key:Long userId<br/>
	 *  value:CopyOnWriteArrayList<DownloadTsTask> 下载任务列表<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 上午10:05:43
	 */
	public static class Queue{
		
		private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<DownloadTsTask>> queue = new ConcurrentHashMap<Long, CopyOnWriteArrayList<DownloadTsTask>>();
		
		/**
		 * 向队列中添加任务<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年10月21日 上午10:11:29
		 * @param Long userId 用户id
		 * @param DownloadTsTask task 任务
		 */
		public static void push(Long userId, DownloadTsTask task){
			if(!queue.contains(userId)){
				queue.put(userId, new CopyOnWriteArrayList<DownloadTsTask>());
			}
			queue.get(userId).add(task);
		}
		
		/**
		 * 查询任务<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年10月21日 上午10:12:07
		 * @param Long userId 用户id
		 * @param String taskId 任务id
		 * @return DownloadTsTask 任务
		 */
		public static DownloadTsTask find(Long userId, String taskId){
			if(!queue.containsKey(userId) || queue.get(userId)==null) return null;
			CopyOnWriteArrayList<DownloadTsTask> tasks = queue.get(userId);
			for(DownloadTsTask task:tasks){
				if(task.getId().equals(taskId)){
					return task;
				}
			}
			return null;
		} 
		
		/**
		 * 查询任务列表<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年10月21日 上午10:12:47
		 * @param Long userId 用户id
		 * @return List<DownloadTsTask> 任务列表
		 */
		public static List<DownloadTsTask> find(Long userId){
			if(!queue.containsKey(userId) || queue.get(userId)==null) return null;
			List<DownloadTsTask> myTasks = new ArrayList<DownloadTsTask>();
			CopyOnWriteArrayList<DownloadTsTask> tasks = queue.get(userId);
			for(DownloadTsTask task:tasks){
				myTasks.add(task);
			}
			return myTasks;
		}
		
		/**
		 * 清除任务<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年10月21日 上午10:18:58
		 * @param Long userId 用户id
		 * @param String taskId 任务id
		 * @return DownloadTsTask 删除的任务
		 */
		public static DownloadTsTask remove(Long userId, String taskId){
			if(!queue.containsKey(userId) || queue.get(userId)==null) return null;
			CopyOnWriteArrayList<DownloadTsTask> tasks = queue.get(userId);
			for(DownloadTsTask task:tasks){
				if(task.getId().equals(taskId)){
					tasks.remove(task);
					return task;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 构造方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午3:38:51
	 * @param Long userId 当前用户id
	 * @param String m3u8Url m3u8 http地址
	 * @param mergedTsName 合并文件名称
	 * @param Callback onReady(this) 下载就绪
	 * @param Callback onError(this) 下载失败
	 * @param int connectionRequestTimeout 从连接池拿链接的超时时间
	 * @Param int connectTimeout 链接建立的超时时间
	 * @param int socketTimeout 数据包传输间隔超时时间
	 */
	public DownloadTsTask(
			Long userId, 
			String m3u8Url, 
			String mergedTsName,
			Callback onReady,
			Callback onError,
			int connectionRequestTimeout,
			int connectTimeout,
			int socketTimeout) throws Exception{
		
		this(userId, m3u8Url, mergedTsName, onReady, onError, null, null, null, connectionRequestTimeout, connectTimeout, socketTimeout);
	}
	
	/**
	 * 构造方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午3:38:51
	 * @param Long userId 当前用户id
	 * @param String m3u8Url m3u8 http地址
	 * @param mergedTsName 合并文件名称
	 * @param Callback onReady(this) 下载就绪
	 * @param Callback onError(this) 下载失败
	 */
	public DownloadTsTask(
			Long userId, 
			String m3u8Url, 
			String mergedTsName,
			Callback onReady,
			Callback onError) throws Exception{
		this(userId, m3u8Url, mergedTsName, onReady, onError, 6000, 6000, 12000);
	}
	
	/**
	 * 构造方法<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午3:38:51
	 * @param Long userId 当前用户id
	 * @param String m3u8Url m3u8 http地址
	 * @param mergedTsName 合并文件名称
	 * @param Callback onReady(this) 下载就绪
	 * @param Callback onError(this) 下载失败
	 * @param Date fileStartTime 文件开始时间
	 * @param Date timeScopeLowerLimit 下载时间下限
	 * @param Date timeScopeUpperLimit 下载时间上限
	 * @param int connectionRequestTimeout 从连接池拿链接的超时时间
	 * @Param int connectTimeout 链接建立的超时时间
	 * @param int socketTimeout 数据包传输间隔超时时间
	 */
	public DownloadTsTask(
			Long userId, 
			String m3u8Url, 
			String mergedTsName,
			Callback onReady,
			Callback onError,
			Date fileStartTime,
			Date timeScopeLowerLimit,
			Date timeScopeUpperLimit,
			int connectionRequestTimeout,
			int connectTimeout,
			int socketTimeout) throws Exception{
		
		this.userId = userId;
		this.setFileStartTime(fileStartTime);
		this.setTimeScopeLowerLimit(timeScopeLowerLimit);
		this.setTimeScopeUpperLimit(timeScopeUpperLimit);
		this.id = new StringBufferWrapper().append(userId).append("_").append(new Date().getTime()).toString();
		URL url = new URL(m3u8Url);
		this.ip = url.getHost();
		this.port = String.valueOf(url.getPort());
		String[] pathes = url.getPath().split("/");
		StringBuffer contextPathBuffer = new StringBuffer();
		contextPathBuffer.append("/");
		for(int i=1; i<pathes.length-1; i++){
			contextPathBuffer.append(pathes[i]).append("/");
		}
		this.contextPath = contextPathBuffer.toString();
		this.basehttpPath = new StringBufferWrapper().append("http://").append(this.ip).append(":").append(this.port).append(this.contextPath).toString();
		Path path = SpringContext.getBean(Path.class);
		this.baseStorePath = new StringBufferWrapper().append(path.webappPath()).append(TS_DOWNLOAD_TMP_FOLDER).append(File.separator).append(userId).append(File.separator).append(this.id).append(File.separator).toString();
		this.m3u8HttpPath = m3u8Url;
		this.m3u8Name = pathes[pathes.length-1];
		this.m3u8StorePath = new StringBufferWrapper().append(this.baseStorePath).append(this.m3u8Name).toString();
		
		this.m3u8Downloaded = false;
		this.m3u8Parsed = false;
		this.merge = false;
		
		this.tsFiles = new ArrayList<DownloadTsTask.TsFile>();
		this.mergedTsName = mergedTsName;
		this.mergedTsLocalHttpPath = new StringBufferWrapper().append("/").append(TS_DOWNLOAD_TMP_FOLDER).append("/").append(this.userId).append("/").append(this.id).append("/merge/").append(URLEncoder.encode(this.mergedTsName)).toString();
		this.mergedTsStorePath = new StringBufferWrapper().append(this.baseStorePath).append("merge").append(File.separator).append(this.mergedTsName).toString();
	
		this.onReady = onReady;
		this.onError = onError;
		
		this.httpClient = HttpClients.createDefault();
		this.httpConfig = RequestConfig.custom()
									   .setConnectionRequestTimeout(connectionRequestTimeout)
									   .setConnectTimeout(connectTimeout)
									   .setSocketTimeout(socketTimeout)
									   .build();
	}

	/**
	 * 启动任务<br/>
	 * <p>
	 * 	1.下载m3u8<br/>
	 *  2.解析m3u8<br/>
	 *  3.下载ts<br/>
	 *  4.合并ts<br/>
	 *  5.返回合屏后ts下载地址<br>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午4:18:37
	 * @return DownloadTsTask 任务数据
	 */
	public DownloadTsTask run(){
		final DownloadTsTask self = this;
		Queue.push(self.getUserId(), self);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					self.downloadAndParseM3u8();
					self.downloadTs();
					self.mergeTs();
					if(self.onReady != null){
						self.onReady.notify(self, null);
					}
					Queue.remove(self.getUserId(), self.getId());
				}catch (Exception e){
					e.printStackTrace();
					if(self.onError != null){
						self.onError.notify(self, e);
					}
					Queue.remove(self.getUserId(), self.getId());
				}
			}
		}).run();
		return self;
	}
	
	/**
	 * 下载并解析m3u8文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午5:46:21
	 */
	public void downloadAndParseM3u8() throws Exception{

		List<String> tsNames = new ArrayList<String>();
		List<Integer> durations = new ArrayList<Integer>();
		HttpGet post = new HttpGet(this.getM3u8HttpPath());
		post.setConfig(this.getHttpConfig());
		CloseableHttpResponse response = this.getHttpClient().execute(post);
		File file = new File(this.getM3u8StorePath());
		File folder = file.getParentFile();
		if(!folder.exists()) folder.mkdirs();
		if(!file.exists()) file.createNewFile();
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		FileWriter fileWriter = null;
		BufferedWriter writer = null;
		try{
			if(response.getStatusLine().getStatusCode() == 200){
				inputStreamReader = new InputStreamReader(response.getEntity().getContent());
				reader = new BufferedReader(inputStreamReader);
				fileWriter = new FileWriter(file);
				writer = new BufferedWriter(fileWriter);
				String line = "";
				while(true){
					line = reader.readLine();
					if(line == null) break;
					writer.write(new StringBufferWrapper().append(line).append("\n").toString());
					if(line.endsWith(".ts")){
						tsNames.add(line);
					}else if(line.startsWith("#EXTINF:")){
						durations.add(Integer.valueOf((String.valueOf(Float.valueOf(line.replace("#EXTINF:", "").split(",")[0])*1000).split("\\.")[0])));
					}
				}
				writer.flush();
				this.setM3u8Downloaded(true);
			}else{
				throw new DownloadM3u8FailException(this.getUserId(), this.getM3u8HttpPath(), EntityUtils.toString(response.getEntity(), "utf-8"));
			}
		}finally{
			if(inputStreamReader != null) inputStreamReader.close();
			if(reader != null) reader.close();
			if(writer != null) writer.close();
			if(fileWriter != null) fileWriter.close();
			EntityUtils.consume(response.getEntity());
		}
		if(tsNames.size() > 0){
			Date timpTime = this.getFileStartTime();
			for(int i=0; i<tsNames.size(); i++){
				String tsName = tsNames.get(i);
				TsFile tsFile = new TsFile();
				tsFile.setDownloaded(false);
				tsFile.setName(tsName);
				tsFile.setHttpPath(new StringBufferWrapper().append(this.getBasehttpPath()).append(tsName).toString());
				tsFile.setStorePath(new StringBufferWrapper().append(this.getBaseStorePath()).append(tsName).toString());
				this.getTsFiles().add(tsFile);
				if(timpTime != null){
					Integer duration = durations.get(i);
					tsFile.setStartTime(timpTime);
					timpTime = DateUtil.addMilliSecond(timpTime, duration);
					tsFile.setEndTime(timpTime);
				}
			}
		}
		this.setM3u8Parsed(true);
	}
	
	/**
	 * 下载ts文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午5:46:24
	 */
	public void downloadTs() throws Exception{
		HttpGet post = null;
		CloseableHttpResponse response = null;
		for(TsFile tsFile:this.getTsFiles()){
			if(this.getFileStartTime() != null){
				if(!((this.getTimeScopeLowerLimit().after(tsFile.getStartTime())&&tsFile.getEndTime().after(this.getTimeScopeLowerLimit())) || 
						(this.getTimeScopeUpperLimit().after(tsFile.getStartTime())&&tsFile.getEndTime().after(this.getTimeScopeUpperLimit())) ||
						(this.getTimeScopeLowerLimit().after(tsFile.getStartTime())&&this.getTimeScopeUpperLimit().after(tsFile.getEndTime())))){
					continue;
				}
			}
			post = new HttpGet(tsFile.getHttpPath());
			post.setConfig(this.getHttpConfig());
			response = this.getHttpClient().execute(post);
			OutputStream outputStream = null;
			try{
				if(response.getStatusLine().getStatusCode() == 200){
					File file = new File(tsFile.getStorePath());
					File folder = file.getParentFile();
					if(!folder.exists()) folder.mkdirs();
					if(!file.exists()) file.createNewFile();
					outputStream = new FileOutputStream(tsFile.getStorePath());    
					byte[] buffer = new byte[1024]; 
					while(true){
						int bytesRead = response.getEntity().getContent().read(buffer);     
						if(bytesRead == -1) break;
						outputStream.write(buffer, 0, bytesRead);
					}
					outputStream.flush();
					tsFile.setDownloaded(true);
					tsFile.setSize(ByteUtil.getSize(file.length()));
				}else{
					throw new DownloadTsFailException(this.getUserId(), this.getM3u8HttpPath(), tsFile.getHttpPath(), EntityUtils.toString(response.getEntity(), "utf-8"));
				}
			}finally{
				if(outputStream != null) outputStream.close();
				EntityUtils.consume(response.getEntity());
			}
		}
	}
	
	/**
	 * 执行shell合并ts文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月18日 下午5:46:29
	 */
	public void mergeTs() throws Exception{
		File file = new File(this.getMergedTsStorePath());
		File folder = file.getParentFile();
		if(!folder.exists()) folder.mkdir();
		
		String platform = System.getProperty("os.name").toLowerCase();
		String cmd = "";
 		if(platform.indexOf("windows") >= 0){
			cmd = "cmd /c type";
			cmd = new StringBufferWrapper().append(cmd).append(" ")
								   		  .append(this.getBaseStorePath()).append("*.ts")
								   		  .append(" > ")
								   		  .append(this.getMergedTsStorePath())
								   		  .toString();
			System.out.println(cmd);
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			this.setMerge(true);
 		}else if(platform.indexOf("linux") >= 0){
			cmd = new StringBufferWrapper().append("cat ")
					  .append(this.getBaseStorePath()).append("*.ts")
			   		  .append(" > ")
			   		  .append(this.getMergedTsStorePath())
			   		  .toString();
			
			System.out.println(cmd);
			String[] cmdArray = {"/bin/sh", "-c", cmd};

			Process process = Runtime.getRuntime().exec(cmdArray);
			process.waitFor();
			this.setMerge(true);
		}
	}
	
	/**
	 * 清除临时文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月23日 下午1:49:01
	 * @param String mergedTsLocalHttpPath 合并后的文件路径
	 */
	public static void clear(String mergedTsLocalHttpPath) throws Exception{
		Path path = SpringContext.getBean(Path.class);
		File file = new File(new StringBufferWrapper().append(path.webappPath()).append(mergedTsLocalHttpPath.substring(1, mergedTsLocalHttpPath.length()).replace("/", File.separator)).toString());
		if(!file.exists()) return;
		File mergeFolder = file.getParentFile();
		File taskFolder = mergeFolder.getParentFile();
		file.delete();
		File[] files = taskFolder.listFiles();
		if(files!=null && files.length>0){
			for(File tmpFile:files){
				tmpFile.delete();
			}
		}
		taskFolder.delete();
	}
	
	public static void main(String[] args) throws Exception{
		Process process = Runtime.getRuntime().exec("cmd /c type D:\\code\\BVC2.0\\bvc2.0\\app\\bvc2.0-integration-it3.1\\bvc2.0-web\\src\\main\\webapp\\ts_download_tmp\\1\\1_1571715344932\\*.ts > D:\\code\\BVC2.0\\bvc2.0\\app\\bvc2.0-integration-it3.1\\bvc2.0-web\\src\\main\\webapp\\ts_download_tmp\\1\\1_1571715344932\\merge\\321-主席-视频解码1-2019-10-22-101145.ts");
		process.waitFor();
	}
	
}
