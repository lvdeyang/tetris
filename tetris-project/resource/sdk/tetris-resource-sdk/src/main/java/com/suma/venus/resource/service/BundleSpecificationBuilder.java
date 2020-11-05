package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.suma.venus.resource.pojo.BundlePO;

public class BundleSpecificationBuilder {

	/**
	 * 
	 * 
	 * 
	 * @param deviceModel
	 * @param sourceType
	 * @param keyword
	 * @param folderId
	 * @param pageable
	 * @return
	 */
	public static Specification<BundlePO> getBundleSpecification(String deviceModel, String sourceType, String keyword, Long folderId, boolean withoutFolder) {

		return new Specification<BundlePO>() {

			@Override
			public Predicate toPredicate(Root<BundlePO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO
				List<Predicate> predicateList = new ArrayList<>();

				// deviceModel 类型
				if (!StringUtils.isEmpty(deviceModel)) {
					predicateList.add(cb.equal(root.get("deviceModel").as(String.class), deviceModel));
				}

				if (!StringUtils.isEmpty(sourceType)) {
					predicateList.add(cb.equal(root.get("sourceType").as(String.class), sourceType));
				}

				if (!StringUtils.isEmpty(keyword)) {

					List<Predicate> bundleKeywordPredicateList = new ArrayList<>();

					bundleKeywordPredicateList.add(cb.like(root.get("bundleName").as(String.class), "%" + keyword + "%"));
					bundleKeywordPredicateList.add(cb.like(root.get("username").as(String.class), "%" + keyword + "%"));

					Predicate[] preKeywords = new Predicate[bundleKeywordPredicateList.size()];
					predicateList.add(cb.or(bundleKeywordPredicateList.toArray(preKeywords)));

				}

				if (withoutFolder) {
					predicateList.add(cb.isNull(root.get("folderId")));
				} else {
					if (!StringUtils.isEmpty(folderId)) {
						predicateList.add(cb.equal(root.get("folderId").as(Long.class), folderId));
					}
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		};
	}
	
	/**
	 * 学习使用SpringDataJPA之Specification复杂查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月16日 下午5:09:53
	 */
	public static Specification<BundlePO> getBundleSpecification(String deviceModel, String sourceType, String keyword, Long userId) {

		return new Specification<BundlePO>() {

			@Override
			public Predicate toPredicate(Root<BundlePO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

				List<Predicate> predicateList = new ArrayList<>();

				// deviceModel 类型
				if (!StringUtils.isEmpty(deviceModel)) {
					predicateList.add(cb.equal(root.get("deviceModel").as(String.class), deviceModel));
				}

				if (!StringUtils.isEmpty(sourceType)) {
					predicateList.add(cb.equal(root.get("sourceType").as(String.class), sourceType));
				}

				if (!StringUtils.isEmpty(keyword)) {

					List<Predicate> bundleKeywordPredicateList = new ArrayList<>();

					bundleKeywordPredicateList.add(cb.like(root.get("bundleName").as(String.class), "%" + keyword + "%"));
					bundleKeywordPredicateList.add(cb.like(root.get("username").as(String.class), "%" + keyword + "%"));

					Predicate[] preKeywords = new Predicate[bundleKeywordPredicateList.size()];
					predicateList.add(cb.or(bundleKeywordPredicateList.toArray(preKeywords)));

				}

				if (null != userId && userId > 0){
					predicateList.add(cb.equal(root.get("userId").as(Long.class), userId));
				}

				Predicate[] pre = new Predicate[predicateList.size()];
				return query.where(predicateList.toArray(pre)).getRestriction();

			}
		};
	}
}
