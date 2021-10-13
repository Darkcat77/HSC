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

	/** 삭제 여부 */
	private String deleteYn;
	
	/** 사용 여부 */
	private String useYn;

	/** 등록일 */
	private LocalDateTime regDate;
	
	/** 수정일 */
	private LocalDateTime uptDate;
	

}
