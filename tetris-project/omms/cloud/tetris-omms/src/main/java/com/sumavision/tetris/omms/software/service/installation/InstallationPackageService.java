package com.sumavision.tetris.omms.software.service.installation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.omms.software.service.installation.exception.EmptyVersionIniException;
import com.sumavision.tetris.omms.software.service.installation.exception.InstallationPackageAlreadyExistException;
import com.sumavision.tetris.omms.software.service.installation.exception.VersionIniNotFoundException;
import com.sumavision.tetris.omms.software.service.type.ServiceTypeDAO;
import com.sumavision.tetris.omms.software.service.type.ServiceTypePO;
import com.sumavision.tetris.omms.software.service.type.exception.ServiceTypeNotFoundException;

@Service
public class InstallationPackageService {

	@Autowired
	private ServiceTypeDAO serviceTypeDao;
	
	@Autowired
	private InstallationPackageDAO installationPackageDao;
	
	@Autowired
	private PropertiesDAO propertiesDao;
	
	@Autowired
	private Path path;
	
	@Autowired
	private ProcessDAO processDAO;
	
	/**
	 * 添加版本<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月27日 下午5:55:54
	 * @param Long serviceTypeId 服务id
	 * @param String creator 创建者
	 * @param String remark 备注
	 * @param FileItem installationPackage 安装包内容
	 * @return InstallationPackageVO 安装包
	 */
	@Transactional
	public InstallationPackageVO add(
			Long serviceTypeId,
			String creator,
			String remark,
			FileItem installationPackage) throws Exception{
		
		ServiceTypePO serviceType = serviceTypeDao.findOne(serviceTypeId);
		if(serviceType == null){
			throw new ServiceTypeNotFoundException(serviceTypeId);
		}
		String fileFolderPath = new StringBufferWrapper().append(File.separator).append("packages").append(File.separator).append(serviceType.getName()).toString();
		String filePath = new StringBufferWrapper().append(fileFolderPath).append(File.separator).append(installationPackage.getName()).toString();
		String fullPath = new StringBufferWrapper().append(path.webappPath()).append(filePath).toString();
		File testFile = new File(fullPath);
		if(testFile.exists()){
			throw new InstallationPackageAlreadyExistException(serviceType.getName(), installationPackage.getName());
		}
		
		File folderFile = new File(new StringBufferWrapper().append(path.webappPath()).append(File.separator).append(fileFolderPath).toString());
		if(!folderFile.exists()) folderFile.mkdirs();
		
		InputStream block = null;
		FileOutputStream out = null;
		ZipFile zipFile = null;
		
		try{
			testFile.createNewFile();
			block = installationPackage.getInputStream();
			out = new FileOutputStream(testFile);
			int buffSize = 1024*1024*10;
			byte[] buff = new byte[buffSize];  
	        int rc = 0;  
	        while ((rc = block.read(buff, 0, buffSize)) > 0) {  
	            out.write(buff, 0, rc);  
	        }  
	        
			zipFile = new ZipFile(testFile);
			String zipRoot = zipFile.entries().nextElement().getName();
			ZipEntry versionFile = zipFile.getEntry(new StringBufferWrapper().append(zipRoot).append("version.ini").toString());
			if(versionFile == null){
				throw new VersionIniNotFoundException();
			}
			String version = FileUtil.readAsString(zipFile.getInputStream(versionFile));
			if(version==null || version.equals("")){
				throw new EmptyVersionIniException();
			}
			
			Date now = new Date();
			InstallationPackagePO packageEntity = new InstallationPackagePO();
			packageEntity.setServiceTypeId(serviceTypeId);
			packageEntity.setVersion(version);
			packageEntity.setCreateTime(now);
			packageEntity.setCreator(creator);
			packageEntity.setRemark(remark);
			packageEntity.setFilePath(filePath);
			packageEntity.setFileName(installationPackage.getName());
			installationPackageDao.save(packageEntity);
			
			ZipEntry configDescriptionFile = zipFile.getEntry(new StringBufferWrapper().append(zipRoot).append("config.description.json").toString());
			if(configDescriptionFile != null){
				List<PropertiesPO> properties = new ArrayList<PropertiesPO>();
				List<ProcessPO> processes = new ArrayList<ProcessPO>();
				String configDescription = FileUtil.readAsString(zipFile.getInputStream(configDescriptionFile));
				/*JSONArray descriptions = JSON.parseArray(configDescription);
				for(int i=0; i<descriptions.size(); i++){
					JSONObject description = descriptions.getJSONObject(i);
					PropertiesPO property = new PropertiesPO();
					property.setUpdateTime(now);
					property.setPropertyKey(description.getString("key"));
					property.setPropertyName(description.getString("name"));
					property.setValueType(PropertyValueType.valueOf(description.getString("valueType").toUpperCase()));
					if(PropertyValueType.ENUM.equals(property.getValueType())){
						property.setValueSelect(description.getString("valueSelect"));
					}
					property.setPropertyDefaultValue(description.getString("defaultValue"));
					property.setInstallationPackageId(packageEntity.getId());
					properties.add(property);
				}*/
				JSONObject jsonObject = JSONObject.parseObject(configDescription);
				JSONArray arr1 = jsonObject.getJSONArray("config");
				for(int i = 0; i < arr1.size(); i++){
					JSONObject description = arr1.getJSONObject(i);
					PropertiesPO property = new PropertiesPO();
					property.setUpdateTime(now);
					property.setPropertyKey(description.getString("key"));
					property.setPropertyName(description.getString("name"));
					property.setValueType(PropertyValueType.valueOf(description.getString("valueType").toUpperCase()));
					if(PropertyValueType.ENUM.equals(property.getValueType())){
						property.setValueSelect(description.getString("valueSelect"));
					}
					property.setPropertyDefaultValue(description.getString("defaultValue"));
					property.setInstallationPackageId(packageEntity.getId());
					properties.add(property);
				}
				propertiesDao.save(properties);
				
				JSONArray arr2 = jsonObject.getJSONArray("process");
				for(int i = 0; i < arr2.size(); i++){
					JSONObject description2 = arr2.getJSONObject(i);
					ProcessPO process = new ProcessPO();
					process.setProcessId(description2.getString("id"));
					process.setProcessName(description2.getString("name"));
					process.setInstallationPackageId(packageEntity.getId());
					processes.add(process);
				}
				processDAO.save(processes);
			}
			
			return new InstallationPackageVO().set(packageEntity);
		}catch(Exception e){
			if(out != null) out.close();
			if(zipFile != null) zipFile.close();
			if(testFile.exists()){
				testFile.delete();
			} 
			throw e;
		}finally{
			if(block != null) block.close();
			if(out != null) out.close();
			if(zipFile != null) zipFile.close();
		}
	}
	
	/**
	 * 删除版本<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月28日 下午1:19:11
	 * @param Long id 版本id
	 */
	@Transactional
	public void remove(Long id) throws Exception{
		InstallationPackagePO packageEntity = installationPackageDao.findOne(id);
		if(packageEntity != null){
			List<PropertiesPO> properties = propertiesDao.findByInstallationPackageId(packageEntity.getId());
			if(properties!=null && properties.size()>0){
				propertiesDao.deleteInBatch(properties);
			}
			installationPackageDao.delete(packageEntity);
			String fullPath = new StringBufferWrapper().append(path.webappPath()).append(packageEntity.getFilePath()).toString();
			File testFile = new File(fullPath);
			if(testFile.exists()) testFile.delete();
		}
	}
	
}
