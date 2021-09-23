package com.hsc.domain;

import java.time.LocalDateTime;

import com.hsc.paging.Criteria;
import com.hsc.paging.PaginationInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDTO extends Criteria {

	/** 페이징 정보 */
	private PaginationInfo paginationInfo;

	/** 종료 여부 */
	private String endYn;

	/** 등록일 */
	private LocalDateTime regDate;
	
	/** 수정일 */
	private LocalDateTime uptDate;
	

}
