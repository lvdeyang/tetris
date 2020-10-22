package com.suma.venus.alarmoprlog.service.oprlog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.suma.venus.alarmoprlog.orm.dao.IOprlogDAO;
import com.suma.venus.alarmoprlog.orm.entity.OprlogPO;
import com.suma.venus.alarmoprlog.service.oprlog.vo.QueryOprlogVO;
import com.sumavision.tetris.alarm.bo.OprlogParamBO.EOprlogType;
import com.sumavision.tetris.commons.util.date.DateUtil;

/**
 * 
 * 
 * @author chenmo
 *
 */
@Service
public class OprlogService {

	@Autowired
	private IOprlogDAO oprlogDAO;

	public Page<OprlogPO> queryOprlogByQueryPage(QueryOprlogVO queryOprlogVO, Pageable pageable) {

		Page<OprlogPO> oprlogPOPage = oprlogDAO.findAll(new Specification<OprlogPO>() {

			private static final long serialVersionUID = -2068822745503586360L;

			@Override
			public Predicate toPredicate(Root<OprlogPO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicateList = new ArrayList<>();

				if (!StringUtils.isEmpty(queryOprlogVO.getOprlogType())) {

					if (queryOprlogVO.getOprlogType().equals(EOprlogType.USER_OPR.toString())) {
						List<Predicate> oprlogTypePredicateList = new ArrayList<>();

						oprlogTypePredicateList.add(criteriaBuilder.equal(root.get("oprlogType").as(String.class),
								queryOprlogVO.getOprlogType()));
						oprlogTypePredicateList.add(criteriaBuilder.equal(root.get("oprlogType").as(String.class),
								criteriaBuilder.nullLiteral(String.class)));

						Predicate[] preOprlogTypes = new Predicate[oprlogTypePredicateList.size()];
						predicateList.add(criteriaBuilder.or(oprlogTypePredicateList.toArray(preOprlogTypes)));

					} else {
						predicateList.add(criteriaBuilder.equal(root.get("oprlogType").as(String.class),
								queryOprlogVO.getOprlogType()));
					}
				}

				if (!StringUtils.isEmpty(queryOprlogVO.getUserName())) {

					predicateList.add(criteriaBuilder.like(root.get("userName").as(String.class),
							"%" + queryOprlogVO.getUserName() + "%"));
				}

				if (!StringUtils.isEmpty(queryOprlogVO.getSourceService())) {

					predicateList.add(criteriaBuilder.like(root.get("sourceService").as(String.class),
							"%" + queryOprlogVO.getSourceService() + "%"));
				}

				if (!StringUtils.isEmpty(queryOprlogVO.getOprName())) {

					predicateList.add(criteriaBuilder.like(root.get("oprName").as(String.class),
							"%" + queryOprlogVO.getOprName() + "%"));
				}

				if (queryOprlogVO.getQueryTimeStart() != null) {
					try {
						predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("oprTime").as(Date.class),
								DateUtil.parse(queryOprlogVO.getQueryTimeStart(), "yyyy-MM-dd hh:mm:ss")));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (queryOprlogVO.getQueryTimeEnd() != null) {
					try {
						predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("oprTime").as(Date.class),
								DateUtil.parse(queryOprlogVO.getQueryTimeEnd(), "yyyy-MM-dd hh:mm:ss")));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		}, pageable);

		return oprlogPOPage;
	}

}
