package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScreenService {
	@Autowired
	private ScreenQuery screenQeury;
	
	@Autowired
	private ScreenDAO screenDao;
	
	@Autowired
	private ProgramQuery programQuery;
	
	/**
	 * 编辑媒资排单表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programVO 分屏信息
	 * @return List<ScreenVO> 编辑后的媒资排单
	 */
	public List<ScreenVO> setScreenInfo(Long programId, List<ScreenVO> screenList) throws Exception {
		if (programId == null || screenList == null)
			return null;

		this.removeScreen(programId);
		List<ScreenPO> screenPOList = new ArrayList<ScreenPO>();
		List<ScreenVO> screenVOList = new ArrayList<ScreenVO>();

		if (screenList.isEmpty()) return screenVOList;
		for (ScreenVO screenVO : screenList) {
			ScreenPO screenPO = ScreenVO.getPO(screenVO, programId);

			screenPOList.add(screenPO);
		}
		screenDao.save(screenPOList);
		screenVOList.addAll(ScreenVO.getConverter(ScreenVO.class).convert(screenPOList, ScreenVO.class));

		return screenVOList;
	}

	/**
	 * 根据分屏id删除排单列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param programId 分屏id
	 */
	public void removeScreen(Long programId) {
		List<ScreenPO> screenList = screenDao.findByProgramId(programId);
		screenDao.deleteInBatch(screenList);
	}
	
	/**
	 * cs内频道下的媒资id删除时的处理(删除所有有关排单，更新排单顺序)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param resourceId cs内的媒资id
	 */
	public void dealWithResourceRemove(Long channelId,Long resourceId) throws Exception{
		ProgramVO program = programQuery.getProgram(channelId);
		if (program == null) {
			return;
		}
		Long programId = program.getId();
		List<ScreenPO> screenPOs = screenDao.findByProgramIdAndResourceId(programId, resourceId);
		screenDao.deleteInBatch(screenPOs);
		if (screenPOs != null && !screenPOs.isEmpty()) {
			List<ScreenPO> needSaveList = new ArrayList<ScreenPO>();
			for (int i = screenPOs.size() - 1; i >= 0; i--) {
				Long serialNum = screenPOs.get(i).getSerialNum();
				Long screenIndex = screenPOs.get(i).getScreenIndex();
				List<ScreenPO> allScreenList = screenDao.findByProgramIdAndSerialNum(programId, serialNum);
				if (allScreenList != null && allScreenList.size() > 0) {
					for (ScreenPO item : allScreenList) {
						if (item.getScreenIndex() > screenIndex) {
							item.setScreenIndex(item.getScreenIndex() - 1);
							needSaveList.add(item);
						}
					}
				}
			}
			if (needSaveList.size() > 0) {
				screenDao.save(needSaveList);
			}
		}
	}

//	public void dealWithResourceRemove(Long channelId, String mimsUuid) throws Exception {
//		ProgramVO program = programQuery.getProgram(channelId);
//		Long programId = program.getId();
//		List<ScreenPO> removeScreenList = screenDao.findByProgramIdAndMimsUuid(programId, mimsUuid);
//		screenDao.deleteInBatch(removeScreenList);
//		if (removeScreenList != null && removeScreenList.size() > 0) {
//			List<ScreenPO> needSaveList = new ArrayList<ScreenPO>();
//			for (int i = removeScreenList.size() - 1; i >= 0; i--) {
//				Long serialNum = removeScreenList.get(i).getSerialNum();
//				Long screenIndex = removeScreenList.get(i).getScreenIndex();
//				List<ScreenPO> allScreenList = screenDao.findByProgramIdAndSerialNum(programId, serialNum);
//				if (allScreenList != null && allScreenList.size() > 0) {
//					for (ScreenPO item : allScreenList) {
//						if (item.getScreenIndex() > screenIndex) {
//							item.setScreenIndex(item.getScreenIndex() - 1);
//							needSaveList.add(item);
//						}
//					}
//				}
//			}
//			if (needSaveList.size() > 0) {
//				screenDao.save(needSaveList);
//			}
//		}
//	}
}
