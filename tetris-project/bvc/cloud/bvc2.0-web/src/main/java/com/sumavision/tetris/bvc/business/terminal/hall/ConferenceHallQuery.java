package com.sumavision.tetris.bvc.business.terminal.hall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalVO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Component
public class ConferenceHallQuery {

	@Autowired
	private ConferenceHallDAO conferenceHallDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private FolderDao folderDao;
	
	/**
	 * 查询组织机构<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月4日 下午5:44:17
	 * @return List<TreeNodeVO> 组织机构树
	 */
	public List<TreeNodeVO> queryTotalFolders() throws Exception{
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		//查询所有非点播的文件夹以及内部文件夹
		List<FolderPO> totalFolders = resourceService.queryAllFolders();
		for(FolderPO folder:totalFolders){
			if(!FolderType.ON_DEMAND.equals(folder.getFolderType()) && SOURCE_TYPE.SYSTEM.equals(folder.getSourceType())){
				folders.add(new FolderBO().set(folder));
			}
		}
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders);
		}
		return _roots;
	}
	
	/**递归组文件夹层级*/
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders){
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders);
			}
		}
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**
	 * 查询但设备终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年8月3日 下午6:09:24
	 * @return List<TerminalVO> 终端列表
	 */
	public List<TerminalVO> loadSingleBundleTerminal() throws Exception{
		List<TerminalVO> terminals = new ArrayList<TerminalVO>();
		List<TerminalPO> terminalEntities = terminalDao.findAll();
		if(terminalEntities==null || terminalEntities.size()<=0) return terminals;
		Set<Long> terminalIds = new HashSet<Long>();
		for(TerminalPO terminalEntity:terminalEntities){
			terminalIds.add(terminalEntity.getId());
		}
		List<TerminalBundlePO> bundleEntities = terminalBundleDao.findByTerminalIdIn(terminalIds);
		if(bundleEntities==null || bundleEntities.size()<=0) return terminals;
		for(TerminalPO terminalEntity:terminalEntities){
			int countBundle = 0;
			TerminalBundlePO bundle = null;
			for(TerminalBundlePO bundleEntity:bundleEntities){
				if(bundleEntity.getTerminalId().equals(terminalEntity.getId())){
					countBundle+=1;
					if(countBundle > 1) break;
					bundle = bundleEntity;
				}
			}
			if(countBundle == 1){
				terminals.add(new TerminalVO().set(terminalEntity).setSingleBundleMode(bundle.getBundleType()));
			}
		}
		return terminals;
	}
	
	/**
	 * 分页查询会场<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午4:42:00
	 * @param String name 会场名称模糊查询
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<ConferenceHallVO> 会场列表
	 */
	public Map<String, Object> load(
			String name,
			int currentPage, 
			int pageSize) throws Exception{
		Pageable page = new PageRequest(currentPage-1, pageSize);
		Page<ConferenceHallPO> pagedEntities = null;
		if(name==null || "".equals(name)){
			pagedEntities = conferenceHallDao.findAll(page);
		}else{
			pagedEntities = conferenceHallDao.findByNameLike(new StringBufferWrapper().append("%").append(name).append("%").toString(), page);
		}
		long total = pagedEntities.getTotalElements();
		List<ConferenceHallPO> entities = pagedEntities.getContent();
		Set<Long> terminalIds = new HashSet<Long>();
		Set<Long> folderIds = new HashSet<Long>();
		if(entities!=null && entities.size()>0){
			for(ConferenceHallPO entity:entities){
				terminalIds.add(entity.getTerminalId());
				if(entity.getFolderId() != null) folderIds.add(entity.getFolderId());
			}
		}
		List<TerminalPO> terminals = null;
		if(terminalIds.size() > 0){
			terminals = terminalDao.findAll(terminalIds);
		}
		List<FolderPO> folders = null;
		if(folderIds.size() > 0){
			folders = folderDao.findAll(folderIds);
		}
		List<ConferenceHallVO> rows = ConferenceHallVO.getConverter(ConferenceHallVO.class).convert(entities, ConferenceHallVO.class);
		if(terminals!=null 
				&& terminals.size()>0 
				&& rows!=null 
				&& rows.size()>0){
			for(ConferenceHallVO row:rows){
				for(TerminalPO terminal:terminals){
					if(terminal.getId().equals(row.getTerminalId())){
						row.setTerminalName(terminal.getName());
						break;
					}
				}
			}
		}
		if(folders!=null 
				&& folders.size()>0 
				&& rows!=null 
				&& rows.size()>0){
			for(ConferenceHallVO row:rows){
				for(FolderPO folder:folders){
					if(folder.getId().equals(row.getFolderId())){
						row.setFolderName(folder.getName());
						break;
					}
				}
			}
		}
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
}
