
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 上午11:03:47  
* All right reserved.  
*  
*/

package com.sumavision.bvc.repository;

import org.springframework.stereotype.Repository;

import com.sumavision.bvc.PO.PreviewPO;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc: bvc-monitor-ui\
 * @author: kpchen
 * @createTime: 2018年6月5日 上午11:03:47
 * @history:
 * @version: v1.0
 */
@Repository
public interface PreviewRespository extends JpaRepository<PreviewPO, Long> {
}
