package com.sumavision.tetris.omms.hardware.database;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupDAO;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupPO;

@Controller
@RequestMapping(value = "/database")
public class DatabaseController {
	
	@Autowired
	private DatabaseQuery databaseQuery;
	
	@Autowired
	private DatabaseService databaseService;
	
	@Autowired
	private DatabaseBackupDAO databaseBackupDAO;

	/**
	 * 查询sql服务下的数据库名<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月3日 下午5:55:12
	 * @param databaseId sql服务id
	 * @return List<DatabasesVO> 数据库信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/databases")
	public Object queryDatabases(Long id)throws Exception{
		
		return databaseQuery.queryDatabases(id);
	}
	
	/**
	 * 备份数据库<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月9日 下午7:20:43
	 * @param id 数据库服务id
	 * @param databases 备份的数据库id
	 * @param name 备份之后的名字
	 * @param remark 备份之后的说明
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/backup/databases")
	public Object backupDatabases(Long id, @RequestParam String[] databases, String name, String remark) throws Exception{
		
		return databaseService.backupDatabases(id, databases, name, remark);
	}
	
	
	/**
	 * 查询数据库的备份文件<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月10日 上午8:59:59
	 * @param id mysql服务的id
	 * @return List<DatabaseBackupVO> 备份数据库信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value ="/query/backup/databases")
	public Object queryBackupDatabases(Long id)throws Exception{
		
		return databaseQuery.queryBackupDatabases(id);
	}
	
	
	/**
	 * 下载备份数据库到本地<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月10日 下午1:16:53
	 * @param databaseBackupId 数据库备份文件的id
	 * @return name 备份的文件名
	 * @return uri 下载路径
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/backup")
	public ResponseEntity downloadBackup(Long databaseBackupId ,HttpServletResponse response) throws Exception{
		DatabaseBackupPO databaseBackupPO = databaseBackupDAO.findOne(databaseBackupId);
		try {   
		File file = new File(databaseBackupPO.getDownuri());
		if (file.exists()) {   
			String dfileName = file.getName();   
	        InputStream fis = new BufferedInputStream(new FileInputStream( file));   
	        response.reset();   
	        response.setContentType("application/x-download");
	        response.addHeader("Content-Disposition","attachment;filename="+ new String(dfileName.getBytes(),"iso-8859-1"));
	        response.addHeader("Content-Length", "" + file.length());   
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());   
	        response.setContentType("application/octet-stream");   
	        byte[] buffer = new byte[1024 * 1024 * 4];   
	        int i = -1;   
	        while ((i = fis.read(buffer)) != -1) {   
	        	toClient.write(buffer, 0, i);  
	        	}
	        fis.close();
	        toClient.flush();
	        toClient.close();
	        try {
	        	response.wait();
	        	}
	        catch
	        (InterruptedException e)
	        {
	        	e.printStackTrace();
	        }
	        } else {
	        	PrintWriter out = response.getWriter();
	        	out.print("<script>");
	        	out.print("alert(\"not find the file\")");
	        	out.print("</script>");
	        	}   
	         } catch (IOException ex) {
	        	 PrintWriter out = response.getWriter();
	        	 out.print("<script>");
	        	 out.print("alert(\"not find the file\")");
	        	 out.print("</script>");
	        	 }
		return new ResponseEntity(null,null,HttpStatus.OK);
	}

	/**
	 * 恢复数据库<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 上午8:32:28
	 * @param databaseBackupId 恢复的备份数据库地id
	 * @param databaseName 要恢复的数据库名称
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/recover/database")
	public Object recoverDatabase(Long databaseBackupId,String databaseName) throws Exception{
		//数据库id 、 	
		return databaseService.recoverDatabase(databaseBackupId ,databaseName);
	}
	
	/**
	 * 删除数据库的备份<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月21日 上午9:26:53
 	 * @param id 备份数据库的id
	 */   //此处未涉及服务器上数据的删除
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/backup" )
	public Object deleteBackup(Long id)throws Exception{
		
		databaseService.deleteBackup(id);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/database")
	public Object findDatabase(Long id)throws Exception{
		
		return databaseQuery.findDatabase(id);
	}
}
