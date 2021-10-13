package com.hsc.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO extends CommonDTO {

	/** 번호(PK) */
	private Long idx;
	/** 게시구분 */
	private String eType;
	/** 이벤트명 */
	private String evtNm;
	/** 시작일 */
	private String stDate;
	/** 종료일 */
	private String edDate;
	/** 설명 */
	private String contents;
	/** 장소 */
	private String place;
	/** 원본 이미지 */
	private String orgImg;
	/** 저장 이미지 */
	private String savImg;
	/** 파일 변경 여부 */
	private String changeYn;
	/** 파일 인덱스 리스트 */
	private List<Long> fileIdxs;

}
