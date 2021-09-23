package com.hsc.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO extends CommonDTO {

	/** 번호(PK) */
	private Long idx;
	/** 이벤트명 */
	private String evtNm;
	/** 시작일 */
	private LocalDateTime stDate;
	/** 종료일 */
	private LocalDateTime edDate;
	/** 설명 */
	private String contents;
	/** 장소 */
	private String place;
	


}
