package com.sumavision.tetris.omms.hardware.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupDAO;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupPO;
import com.sumavision.tetris.omms.hardware.database.databaseBackup.DatabaseBackupVO;
import com.sumavision.tetris.omms.hardware.database.databases.DatabasesDAO;
import com.sumavision.tetris.omms.hardware.database.databases.DatabasesPO;
import com.sumavision.tetris.omms.hardware.database.databases.DatabasesVO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationDAO;
import com.sumavision.tetris.omms.software.service.installation.BackupInformationPO;

@Component
public class DatabaseQuery {
	
	@Autowired
	private DatabaseDAO databaseDAO;
	
	@Autowired
	private DatabasesDAO databasesDAO;
	
	@Autowired
	private DatabaseBackupDAO databaseBackupDAO;
	
	@Autowired
	private BackupInformationDAO backupInformationDAO;
	
	/**
	 * 查询sql服务下的数据库名<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月3日 下午5:55:12
	 * @param databaseId sql服务id
	 * @return List<DatabasesVO> 数据库信息
	 */
	public List<DatabasesVO> queryDatabases(Long id)throws Exception{
		
		DatabaseMetaData dbMetaData = null;
		PreparedStatement prepareStatement = null;
		Connection con = null;
		ResultSet rs = null;
		
		DatabasePO databasePO = databaseDAO.findOne(id);
		
		StringBufferWrapper urlBufferWrapper = new StringBufferWrapper().append("jdbc:mysql://")
				                               .append(databasePO.getDatabaseIP()).append(":").append(databasePO.getDatabasePort())
				                               .append("/").append("information_schema");
		List<String> databasesname = new ArrayList<String>();
		try {
			if (dbMetaData == null) {
				Class.forName("com.mysql.jdbc.Driver");
				String url = urlBufferWrapper.toString();
				String user = databasePO.getUsername();
				String password = databasePO.getPassword();
				con = DriverManager.getConnection(url, user, password);
				dbMetaData = con.getMetaData();
				
				// 获取statement，preparedStatement
	            String sql = "select SCHEMA_NAME from schemata";
	            prepareStatement = con.prepareStatement(sql);
	            
	            rs = prepareStatement.executeQuery();
	            ResultSetMetaData md = rs.getMetaData();
	            int columnCount = md.getColumnCount();
	            while (rs.next()) {
	                for (int i = 1; i <= columnCount; i++) {
	                	databasesname.add(rs.getObject(i).toString());
	                }
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			 //关闭连接，释放资源
            if (rs != null) {
                rs.close();
            }
            if (prepareStatement != null) {
                prepareStatement.close();
            }
            if (con != null) {
            	con.close();
            }
		}
		if(databasesname != null && !databasesname.isEmpty()){
			List<DatabasesPO> databasesPOs = new ArrayList<DatabasesPO>();
			for (String name : databasesname) {
				if(!name.equalsIgnoreCase("information_schema") && !name.equalsIgnoreCase("performance_schema")){
					DatabasesPO databasesPO = new DatabasesPO();
					databasesPO.setRealName(name);
					databasesPO.setDatabaseId(id);
					databasesPOs.add(databasesPO);
				}
			}
			List<DatabasesPO> existDatabasesPOs = databasesDAO.findByDatabaseId(id);
			List<DatabasesPO> reDatabasesPOs = new ArrayList<DatabasesPO>();
			if (existDatabasesPOs != null && !existDatabasesPOs.isEmpty()) {
				for (DatabasesPO databasesPO : existDatabasesPOs) {
					for (DatabasesPO databases : databasesPOs) {
						if (databasesPO.getRealName().equals(databases.getRealName()) && databasesPO.getDatabaseId().equals(databases.getDatabaseId())) {
							reDatabasesPOs.add(databases);
						}
					}
				}
			}
			databasesPOs.removeAll(reDatabasesPOs);
			databasesDAO.save(databasesPOs);
		}
		List<DatabasesPO> databasesPOs = databasesDAO.findByDatabaseId(id);
		List<DatabasesVO> databasesVOs = new ArrayList<DatabasesVO>();
		if (databasesPOs != null && !databasesPOs.isEmpty()) {
			for (DatabasesPO databasesPO : databasesPOs) {
				DatabasesVO databasesVO = new DatabasesVO().set(databasesPO);
				databasesVOs.add(databasesVO);
			}
			
		}
		return databasesVOs;
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
	public List<DatabaseBackupVO> queryBackupDatabases(Long id)throws Exception{
		List<DatabaseBackupVO> databaseBackupVOs = new ArrayList<DatabaseBackupVO>();
		List<DatabaseBackupPO> databaseBackupPOs = databaseBackupDAO.findByDatabaseId(id);
		Set<Long> backupIds = new HashSet<Long>();
		if(databaseBackupPOs != null && databaseBackupPOs.size() > 0){
			for (DatabaseBackupPO databaseBackupPO : databaseBackupPOs) {
				DatabaseBackupVO daBackupVO = new DatabaseBackupVO().set(databaseBackupPO);
				backupIds.add(databaseBackupPO.getId());
				databaseBackupVOs.add(daBackupVO);
			}
			List<BackupInformationPO> backupInformationPOs = backupInformationDAO.findByDatabaseBackupIdIn(backupIds);
			List<DatabaseBackupVO> prodata = new ArrayList<DatabaseBackupVO>();
			if(null != backupInformationPOs && backupInformationPOs.size()>0){
				for (BackupInformationPO backupInformationPO : backupInformationPOs) {
					for (DatabaseBackupVO databaseBackupVO : databaseBackupVOs) {
						if(backupInformationPO.getDatabaseBackupId().equals(databaseBackupVO.getId())){
							prodata.add(databaseBackupVO);
						}
					}
				}
			}
			databaseBackupVOs.removeAll(prodata);
		}
		return databaseBackupVOs;
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
	public Map<String, String> downloadBackup(Long databaseBackupId)throws Exception{
		DatabaseBackupPO databaseBackupPO = databaseBackupDAO.findOne(databaseBackupId);
		
		
		Map<String, String> data = new HashMapWrapper<String, String>().put("name", databaseBackupPO.getName())
				 .put("uri", databaseBackupPO.getDownuri())
				 .getMap();
		
		return data;
	}
	
	
	 public static void main(String args[]) throws Exception{
		DatabaseMetaData dbMetaData = null;
		PreparedStatement prepareStatement = null;
		Connection con = null;
		ResultSet rs = null;
		
		
		try {
			if (dbMetaData == null) {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://192.165.56.111:3306/information_schema";
				String user = "root";
				String password = "sumavisionrd";
				con = DriverManager.getConnection(url, user, password);
				dbMetaData = con.getMetaData();
				
				// 获取statement，preparedStatement
	            String sql = "select SCHEMA_NAME from schemata";
	            prepareStatement = con.prepareStatement(sql);
	            
	            rs = prepareStatement.executeQuery();
	            List<String> lisStrings = new ArrayList<String>();
	            ResultSetMetaData md = rs.getMetaData();
	            int columnCount = md.getColumnCount();
	            while (rs.next()) {
	                for (int i = 1; i <= columnCount; i++) {
	                    lisStrings.add(rs.getObject(i).toString());
	                }
	            }
	            System.out.println(lisStrings);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
	// 关闭连接，释放资源
            if (rs != null) {
                rs.close();
            }
            if (prepareStatement != null) {
                prepareStatement.close();
            }
            if (con != null) {
            	con.close();
            }
		}
	 }

	public String[] findDatabase(Long id) {
		DatabaseBackupPO databaseBackupPO = databaseBackupDAO.findOne(id);
		if (null != databaseBackupPO.getBackupname() || !"".equals(databaseBackupPO.getBackupname())){
			String[] name = databaseBackupPO.getBackupname().split(" ");
			return name;
		}
		return null;
	} 
}
