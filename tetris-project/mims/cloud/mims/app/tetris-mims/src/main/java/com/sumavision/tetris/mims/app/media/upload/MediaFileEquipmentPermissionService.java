package com.sumavision.tetris.mims.app.media.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.shell.RunShell;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;

@Service
@Transactional(rollbackFor = Exception.class)
public class MediaFileEquipmentPermissionService {
	@Autowired
	private MediaFileEquipmentPermissionDAO mediaFileEquipmentPermissionDAO;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery;
	
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
		String dirName = StringUtils.join(serverPropsQuery.queryProps().getFtpIp().split("\\."));
		String destUrl = new StringBufferWrapper().append("http://")
				.append(equipIp)
				.append(":")
				.append("80")
				.append("/dav")
				.append("/")
				.append(dirName).toString();
		
		if ("video".equals(permissionBO.getMediaType())) {
			channelScheduledExecutorService.schedule(new Runnable() {
				@Override
				public void run() {
					try {
						List<String> shellString = RunShell.runShell(new StringBufferWrapper().append("curl -u root:sumavisionrd -T")
								.append(" ")
								.append(permissionBO.getStoreUrl())
								.append(" ")
								.append(destUrl)
								.append("/")
								.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 0, TimeUnit.MILLISECONDS);
		}
		
		String equipStoreUrl = new StringBufferWrapper().append("/usr/sbin/sumavision/xStreamTool/xStreamNginx/dav/data/dav")
				.append("/")
				.append(dirName)
				.append("/")
				.append(permissionBO.getFileName())
				.toString();
		
		return addPermission(permissionBO, equipIp, new StringBufferWrapper().append(destUrl).append("/").append(permissionBO.getFileName()).toString(), equipStoreUrl);
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
				}
			}
			mediaFileEquipmentPermissionDAO.deleteInBatch(permissionPOs);
		}
		return permissionPOs;
	}
}
