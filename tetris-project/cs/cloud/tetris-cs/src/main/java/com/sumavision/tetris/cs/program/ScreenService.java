package com.sumavision.tetris.cs.program;

import java.util.ArrayList;
import java.util.Date;
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
	
	public List<ScreenVO> setScreenInfo(Long programId, List<ScreenVO> screenList) throws Exception {
		if (programId == null || screenList == null)
			return null;

		this.removeScreen(programId);
		List<ScreenPO> screenPOList = new ArrayList<ScreenPO>();
		List<ScreenVO> screenVOList = new ArrayList<ScreenVO>();

		if (screenList.size() > 0) {
			for (int i = 0; i < screenList.size(); i++) {
				screenList.get(i).setProgramId(programId);

				ScreenVO screenVO = screenList.get(i);

				ScreenPO screenPO = new ScreenPO();
				screenPO.setProgramId(programId);
				screenPO.setMimsUuid(screenVO.getMimsUuid());
				screenPO.setResourceId(screenVO.getResourceId());
				screenPO.setScreenIndex(screenVO.getIndex());
				screenPO.setSerialNum(screenVO.getSerialNum());
				screenPO.setName(screenVO.getName());
				screenPO.setPreviewUrl(screenVO.getPreviewUrl());
				screenPO.setUpdateTime(new Date());

				screenPOList.add(screenPO);
				screenVOList.add(new ScreenVO().set(screenPO));
			}
			screenDao.save(screenPOList);
		}

		return screenVOList;
	}

	public void removeScreen(Long programId) {
		List<ScreenPO> screenList = screenDao.findByProgramId(programId);
		screenDao.deleteInBatch(screenList);
	}
	
	public void dealWithResourceRemove(Long channelId,Long resourceId) throws Exception{
		ProgramVO program = programQuery.getProgram(channelId);
		if (program == null) {
			return;
		}
		Long programId = program.getId();
		List<ScreenPO> screenPOs = screenDao.findByProgramIdAndResourceId(programId, resourceId);
		screenDao.deleteInBatch(screenPOs);
		if (screenPOs != null && screenPOs.size() > 0) {
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
