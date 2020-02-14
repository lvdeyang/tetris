package com.suma.venus.alarmoprlog.service.alarm;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suma.venus.alarmoprlog.orm.dao.IAlarmDAO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;
import com.suma.venus.alarmoprlog.service.alarm.vo.QueryAlarmVO;
import com.sumavision.tetris.alarm.bo.AlarmParamBO;
import com.sumavision.tetris.commons.util.date.DateUtil;

@Service
public class AlarmService {

	@Autowired
	private IAlarmDAO alarmDAO;

	/**
	 * @return
	 */
	public Page<AlarmPO> queryAlarmByQueryVOPage(QueryAlarmVO queryAlarmVO, Pageable pageable) {

		Page<AlarmPO> alarmPOPage = alarmDAO.findAll(new Specification<AlarmPO>() {

			private static final long serialVersionUID = -8254899629564945573L;

			@Override
			public Predicate toPredicate(Root<AlarmPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicateList = new ArrayList<>();

				if (queryAlarmVO.getSourceService() != null && !StringUtils.isEmpty(queryAlarmVO.getSourceService())) {

					predicateList.add(cb.equal(root.get("lastAlarm").get("sourceService").as(String.class),
							queryAlarmVO.getSourceService()));
				}

				if (queryAlarmVO.getSourceIP() != null && !StringUtils.isEmpty(queryAlarmVO.getSourceIP())) {

					// 关联另一种写法
					// Join<AlarmPO, RawAlarmPO> alarmPOJoinIP =
					// root.join(root.getModel().getSingularAttribute("lastAlarm",
					// RawAlarmPO.class), JoinType.LEFT);
					// predicateList.add(cb.equal(alarmPOJoinIP.get("sourceIP").as(String.class),
					// queryAlarmVO.getSourceIP()));

					predicateList.add(cb.equal(root.get("lastAlarm").get("sourceIP").as(String.class),
							queryAlarmVO.getSourceIP()));
				}

				if (!StringUtils.isEmpty(queryAlarmVO.getAlarmBlockStatus())) {

					predicateList
							.add(cb.equal(root.get("lastAlarm").get("alarmInfo").get("blockStatus").as(String.class),
									queryAlarmVO.getAlarmBlockStatus()));
				}

				if (queryAlarmVO.getAlarmCode() != null && !StringUtils.isEmpty(queryAlarmVO.getAlarmCode())) {

					predicateList.add(cb.equal(root.get("lastAlarm").get("alarmInfo").get("alarmCode").as(String.class),
							queryAlarmVO.getAlarmCode()));

				}

				// TODO
				if (!StringUtils.isEmpty(queryAlarmVO.getAlarmStatusArrString())) {
					String[] alarmStatusArr = queryAlarmVO.getAlarmStatusArrString().split(",");

					List<Predicate> alarmStatusOrPredicateList = new ArrayList<>();
					for (String str : alarmStatusArr) {
						alarmStatusOrPredicateList.add(cb.equal(root.get("alarmStatus").as(String.class), str));
					}

					Predicate[] preStatus = new Predicate[alarmStatusOrPredicateList.size()];
					predicateList.add(cb.or(alarmStatusOrPredicateList.toArray(preStatus)));
				}

				/*
				 * if (queryAlarmVO.getAlarmStatus() != null &&
				 * !StringUtils.isEmpty(queryAlarmVO.getAlarmStatus())) { predicateList
				 * .add(cb.equal(root.get("alarmStatus").as(String.class),
				 * queryAlarmVO.getAlarmStatus())); }
				 */

				if (queryAlarmVO.getAlarmLevel() != null && !StringUtils.isEmpty(queryAlarmVO.getAlarmLevel())) {
					Join<AlarmPO, RawAlarmPO> alarmPOJoinLevel = root
							.join(root.getModel().getSingularAttribute("lastAlarm", RawAlarmPO.class), JoinType.LEFT);
					predicateList.add(cb.equal(alarmPOJoinLevel.get("alarmInfo").get("alarmLevel").as(String.class),
							queryAlarmVO.getAlarmLevel()));
				}

				if (queryAlarmVO.getAlarmLevel() != null && !StringUtils.isEmpty(queryAlarmVO.getAlarmLevel())) {
					Join<AlarmPO, RawAlarmPO> alarmPOJoinLevel = root
							.join(root.getModel().getSingularAttribute("lastAlarm", RawAlarmPO.class), JoinType.LEFT);
					predicateList.add(cb.equal(alarmPOJoinLevel.get("alarmInfo").get("alarmLevel").as(String.class),
							queryAlarmVO.getAlarmLevel()));
				}

				if (queryAlarmVO.getFirstCreateTime() != null) {
					try {
						predicateList.add(cb.greaterThanOrEqualTo(root.get("firstCreateTime").as(Date.class),
								DateUtil.parse(queryAlarmVO.getFirstCreateTime())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (queryAlarmVO.getLastCreateTime() != null) {
					try {
						predicateList.add(cb.lessThanOrEqualTo(root.get("lastAlarm").get("createTime").as(Date.class),
								DateUtil.parse(queryAlarmVO.getLastCreateTime())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		}, pageable);

		return alarmPOPage;

	}

	public List<AlarmPO> queryAlarmPOforNewMsg(AlarmParamBO alarmParamBO, EAlarmStatus status) {

		List<AlarmPO> alarmPOs = alarmDAO.findAll(new Specification<AlarmPO>() {

			@Override
			public Predicate toPredicate(Root<AlarmPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicateList = new ArrayList<>();

				predicateList.add(cb.equal(root.get("alarmStatus").as(EAlarmStatus.class), status));

				if (alarmParamBO.getAlarmDevice() != null && !StringUtils.isEmpty(alarmParamBO.getAlarmDevice())) {
					predicateList.add(cb.equal(root.get("lastAlarm").get("alarmDevice").as(String.class),
							alarmParamBO.getAlarmDevice()));
				}

				/*
				 * if (alarmParamBO.getIp() != null &&
				 * !StringUtils.isEmpty(alarmParamBO.getIp())) { predicateList.add(
				 * cb.equal(root.get("lastAlarm").get("sourceIP").as(String.class),
				 * alarmParamBO.getIp())); }
				 */

				if (alarmParamBO.getAlarmCode() != null && !StringUtils.isEmpty(alarmParamBO.getAlarmCode())) {

					predicateList.add(cb.equal(root.get("lastAlarm").get("alarmInfo").get("alarmCode").as(String.class),
							alarmParamBO.getAlarmCode()));
				}

				if (alarmParamBO.getAlarmObj() != null && !StringUtils.isEmpty(alarmParamBO.getAlarmObj())) {
					predicateList.add(cb.equal(root.get("lastAlarm").get("alarmObj").as(String.class),
							alarmParamBO.getAlarmObj()));
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		});

		return alarmPOs;

	}

}
