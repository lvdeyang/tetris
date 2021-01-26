package com.sumavision.tetris.mims.app.media.upload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.shell.RunShell;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaFileEquipmentPermissionService {
	private static final Logger logger = LoggerFactory.getLogger(MediaFileEquipmentPermissionService.class);
	
	@Autowired
	private MediaFileEquipmentPermissionDAO mediaFileEquipmentPermissionDAO;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	private ScheduledExecutorService channelScheduledExecutorService = Executors.newScheduledThreadPool(3);
	
	/**
	 * 批量同步文件到设备<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午5:26:26
	 * @param List<MediaFileEquipmentPermissionBO> permissionBOs
	 * @param String equipIp 设备ip
	 */
	public List<MediaFileEquipmentPermissionPO> addPermissionSend(List<MediaFileEquipmentPermissionBO> permissionBOs, String equipIp) throws Exception {
		if (permissionBOs == null || permissionBOs.isEmpty() || equipIp == null || equipIp.isEmpty()) return new ArrayList<MediaFileEquipmentPermissionPO>();
		List<MediaFileEquipmentPermissionPO> permissionPOs = new ArrayList<MediaFileEquipmentPermissionPO>();
		for (MediaFileEquipmentPermissionBO permissionBO : permissionBOs) {
			permissionPOs.add(addPermissionSend(permissionBO, equipIp));
		}
		return permissionPOs;
	}
	
	/**
	 * 请求文件同步<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午5:38:02
	 * @param MediaFileEquipmentPermissionBO permissionBO 媒资描述
	 * @param String equipIp 设备ip
	 */
	public MediaFileEquipmentPermissionPO addPermissionSend(MediaFileEquipmentPermissionBO permissionBO, String equipIp) throws Exception {
//		String[] cmds={"curl","-u", "root:sumavisionrd",  "-XGET", "http://localhost:18631/rest/v1/system/info","-H"
//		        ,"X-Requested-By:sdc"};
//		ProcessBuilder process = new ProcessBuilder(cmds);
//        Process p;
//        try {
//            p = process.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            StringBuilder builder = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//                builder.append(System.getProperty("line.separator"));
//            }
//        } catch (IOException e) {
//            System.out.print("error");
//            e.printStackTrace();
//        }
//        return null;
		
		FolderPO folder = folderDao.findById(permissionBO.getFolderId());
		MediaFileEquipmentPermissionPO folderPermission = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(folder.getId(), "folder", equipIp);
		if(folderPermission == null){
			throw new BaseException(StatusCode.FORBIDDEN, "文件夹：" + folder.getName() + " 未创建！");
		}
		
		/*String dirName = StringUtils.join(serverPropsQuery.queryProps().getFtpIp().split("\\."));
		String destUrl = new StringBufferWrapper().append("http://")
				.append(equipIp)
				.append(":")
				.append("8914")
				.append("/dav")
				.append("/")
				.append(dirName).toString();*/
		
		if ("video".equals(permissionBO.getMediaType())) {
			channelScheduledExecutorService.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						String sh = new StringBufferWrapper().append("curl -u root:sumavisionrd -T")
								.append(" ")
								.append(permissionBO.getStoreUrl())
								.append(" ")
								.append(folderPermission.getEquipmentHttpUrl())
								.toString();
						List<String> shellString = RunShell.runShell(sh);
						System.out.println(sh + "   " + shellString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 0, TimeUnit.MILLISECONDS);
			
		}else if("picture".equals(permissionBO.getMediaType())){
			try {
				String sh = new StringBufferWrapper().append("curl -u root:sumavisionrd -T")
						.append(" ")
						.append(permissionBO.getStoreUrl())
						.append(" ")
						.append(folderPermission.getEquipmentHttpUrl())
						.toString();
				List<String> shellString = RunShell.runShell(sh);
				System.out.println(sh + "   " + shellString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String equipStoreUrl = new StringBufferWrapper().append(folderPermission.getEquipmentStoreUrl())
														.append(permissionBO.getFileName())
														.toString();
		
		return addPermission(permissionBO, equipIp, new StringBufferWrapper().append(folderPermission.getEquipmentHttpUrl()).append(permissionBO.getFileName()).toString(), equipStoreUrl);
	}
	
	/**
	 * 设备同步文件夹<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月21日 下午4:13:42
	 * @param FolderPO folder 文件夹
	 * @param String equipIp 设备ip
	 * @return MediaFileEquipmentPermissionPO 
	 */ 
	public MediaFileEquipmentPermissionPO addPermissionSend(FolderPO folder, String equipIp) throws Exception {

		MediaFileEquipmentPermissionPO parentPermission = null;
		MediaFileEquipmentPermissionPO folderPermission = null;
		
		if(folder != null){
			parentPermission = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(folder.getParentId(), "folder", equipIp);
			folderPermission = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(folder.getId(), "folder", equipIp);
		}
		
		String folderHttpUrl = null;
		String folderStoreUrl = null;
		if(parentPermission != null){
			String parentHttpUrl = parentPermission.getEquipmentHttpUrl();
			String parentStoreUrl = parentPermission.getEquipmentStoreUrl();
			
			folderHttpUrl = new StringBufferWrapper().append(parentHttpUrl)
													 .append(folder.getName())
													 .append("/")
													 .toString();
			
			folderStoreUrl = new StringBufferWrapper().append(parentStoreUrl)
													  .append(folder.getName())
													  .append("/")
													  .toString();
		}else{
			
			String dirName = StringUtils.join(serverPropsQuery.queryProps().getFtpIp().split("\\."));
			
			String folderHttpDirUrl = new StringBufferWrapper().append("http://")
															   .append(equipIp)
															   .append(":")
															   .append("8914")
															   .append("/dav")
															   .append("/")
															   .append(dirName)
															   .append("/")
															   .toString();
			folderHttpUrl = new StringBufferWrapper().append(folderHttpDirUrl)
													 .append(folder.getName())
													 .append("/")
													 .toString();
			
			folderStoreUrl = new StringBufferWrapper().append("/usr/sbin/sumavision/xStreamNginxData/data/dav")
													  .append("/")
													  .append(dirName)
													  .append("/")
													  .append(folder.getName())
													  .append("/")
													  .toString();
			
			//创建dav
			String sh = new StringBufferWrapper().append("curl -u root:sumavisionrd -v -X MKCOL")
					  .append(" ")
					  .append(folderHttpDirUrl)
					  .toString();
			List<String> shellString = RunShell.runShell(sh);
			System.out.println(sh + "   " + shellString);
		}
		
		if(folderHttpUrl != null && folderStoreUrl != null){
			
			String sh = new StringBufferWrapper().append("curl -u root:sumavisionrd -v -X MKCOL")
					  .append(" ")
					  .append(folderHttpUrl)
					  .toString();
			List<String> shellString = RunShell.runShell(sh);
			System.out.println(sh + "   " + shellString);
			
			if(folderPermission == null){
				folderPermission = new MediaFileEquipmentPermissionPO();
			}
			
			folderPermission.setMediaId(folder.getId());
			folderPermission.setMediaType("folder");
			folderPermission.setUpdateTime(new Date());
			folderPermission.setEquipmentIp(equipIp);
			folderPermission.setEquipmentHttpUrl(folderHttpUrl);
			folderPermission.setEquipmentStoreUrl(folderStoreUrl);
			
			mediaFileEquipmentPermissionDAO.save(folderPermission);
		}
		
		return folderPermission;
	}
	
	/**
	 * 添加关联关系数据<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午3:46:15
	 * @param Long permissionBO 媒资描述
	 * @param String equipIp 设备ip
	 * @param String equipStoreUrl 同步后的绝对路径
	 * @return
	 */
	public MediaFileEquipmentPermissionPO addPermission(MediaFileEquipmentPermissionBO permissionBO, String equipIp, String equipHttpUrl, String equipStoreUrl) throws Exception {
		MediaFileEquipmentPermissionPO permissionPO = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(permissionBO.getMediaId(), permissionBO.getMediaType(), equipIp);
		if (permissionPO == null) permissionPO = new MediaFileEquipmentPermissionPO();
		permissionPO.setMediaId(permissionBO.getMediaId());
		permissionPO.setMediaType(permissionBO.getMediaType());
		permissionPO.setUpdateTime(new Date());
		permissionPO.setEquipmentIp(equipIp);
		permissionPO.setEquipmentHttpUrl(equipHttpUrl);
		permissionPO.setEquipmentStoreUrl(equipStoreUrl);
		mediaFileEquipmentPermissionDAO.save(permissionPO);
		return permissionPO;
	}
	
	/**
	 * 删除关联关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午4:29:37
	 * @param Long mediaId 资源id
	 * @param Long mediaType 资源类型
	 * @param String equipIp 设备ip
	 */
	public MediaFileEquipmentPermissionPO removePermission(MediaFileEquipmentPermissionBO permissionBO, String equipIp) throws Exception {
		MediaFileEquipmentPermissionPO permissionPO = mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(permissionBO.getMediaId(), permissionBO.getMediaType(), equipIp);
		
		RunShell.runShell(new StringBufferWrapper().append("curl -u root:sumavisionrd -v -X DELETE")
				.append(" ")
				.append(permissionPO.getEquipmentHttpUrl())
				.toString());
		if (permissionPO != null) mediaFileEquipmentPermissionDAO.delete(permissionPO);
		return permissionPO;
	}
	
	/**
	 * 根据资源id和类型删除设备上文件和关联关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月11日 下午4:42:51
	 * @param List<Long> mediaIds 资源id
	 * @param String mediaType 资源类型
	 * @return
	 */	
	public List<MediaFileEquipmentPermissionPO> removePermission(List<Long> mediaIds, String mediaType, String equipIp) throws Exception {
		if (mediaIds == null || mediaIds.isEmpty()) return new ArrayList<MediaFileEquipmentPermissionPO>(); 
		List<MediaFileEquipmentPermissionPO> permissionPOs = new ArrayList<MediaFileEquipmentPermissionPO>();
		if (equipIp == null || equipIp.isEmpty()) {
			permissionPOs = mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaType(mediaIds, mediaType);
		} else {
			permissionPOs = mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaTypeAndEquipmentIp(mediaIds, mediaType, equipIp);
		}
		if (permissionPOs != null && !permissionPOs.isEmpty()) {
			for (MediaFileEquipmentPermissionPO permissionPO : permissionPOs) {
				try {
					RunShell.runShell(new StringBufferWrapper().append("curl -u root:sumavisionrd -v -X DELETE")
							.append(" ")
							.append(permissionPO.getEquipmentHttpUrl())
							.toString());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			mediaFileEquipmentPermissionDAO.deleteInBatch(permissionPOs);
		}
		return permissionPOs;
	}
	
	public List<MediaFileEquipmentPermissionPO> removePermission(Long folderId, List<Long> folderIds, List<Long> mediaIds, String mediaType, String equipIp) throws Exception {
		List<MediaFileEquipmentPermissionPO> permissionPOs = new ArrayList<MediaFileEquipmentPermissionPO>();
		List<MediaFileEquipmentPermissionPO> folderPermissionPOs = new ArrayList<MediaFileEquipmentPermissionPO>(); 
		if (equipIp == null || equipIp.isEmpty()) {
			if(!CollectionUtils.isEmpty(mediaIds)){
				permissionPOs.addAll(mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaType(mediaIds, mediaType));
			}
			if(!CollectionUtils.isEmpty(folderIds)){
				permissionPOs.addAll(mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaType(folderIds, "folder"));
			}
			folderPermissionPOs.addAll(mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaType(folderId, "folder"));
		} else {
			if(!CollectionUtils.isEmpty(mediaIds)){
				permissionPOs.addAll(mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaTypeAndEquipmentIp(mediaIds, mediaType, equipIp));
			}
			if(!CollectionUtils.isEmpty(folderIds)){
				permissionPOs.addAll(mediaFileEquipmentPermissionDAO.findByMediaIdInAndMediaTypeAndEquipmentIp(folderIds, "folder", equipIp));
			}
			folderPermissionPOs.add(mediaFileEquipmentPermissionDAO.findByMediaIdAndMediaTypeAndEquipmentIp(folderId, "folder", equipIp));
		}
		if (folderPermissionPOs != null && !folderPermissionPOs.isEmpty()) {
			for (MediaFileEquipmentPermissionPO permissionPO : folderPermissionPOs) {
				try {
					RunShell.runShell(new StringBufferWrapper().append("curl -u root:sumavisionrd -v -X DELETE")
							.append(" ")
							.append(permissionPO.getEquipmentHttpUrl())
							.toString());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			mediaFileEquipmentPermissionDAO.deleteInBatch(folderPermissionPOs);
		}
		if(!CollectionUtils.isEmpty(permissionPOs)){
			mediaFileEquipmentPermissionDAO.deleteInBatch(permissionPOs);
		}
		return folderPermissionPOs;
	}
	
}
