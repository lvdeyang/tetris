package com.sumavision.tetris.record.file;

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


@Service
public class RecordFileService {

	@Autowired
	private RecordFileDAO recordFileDAO;

	/**
	 * @return
	 */
	public Page<RecordFilePO> queryRecordFileByMultiParamsPage(Long recordStrategyId, Pageable pageable) {

		Page<RecordFilePO> recordFilePOPage = recordFileDAO.findAll(new Specification<RecordFilePO>() {

			@Override
			public Predicate toPredicate(Root<RecordFilePO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicateList = new ArrayList<>();

				if (recordStrategyId != null && recordStrategyId != 0) {
					predicateList.add(cb.equal(root.get("recordStrategyId").as(Long.class), recordStrategyId));
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		}, pageable);

		return recordFilePOPage;

	}

}
