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
	/** 이벤트명(영문) */
	private String enEvtNm;
	/** 주최 */
	private String oranized;
	/** 주최(영문) */
	private String enOranized;
	/** 시작일 */
	private String stDate;
	/** 종료일 */
	private String edDate;
	/** 설명 */
	private String contents;
	/** 설명(영문) */
	private String enContents;
	/** 장소 */
	private String place;
	/** 장소 (영문)*/
	private String enPlace;
	/** 관람시간*/
	private String evtTime;
	/** 관람시간(영문)*/
	private String enEvtTime;
	/** 관람료*/
	private String evtFee;
	/** 관람료(영문)*/
	private String enEvtFee;
	/** 원본 이미지 */
	private String orgImg;
	/** 저장 이미지 */
	private String savImg;
	/** 파일 변경 여부 */
	private String changeYn;
	/** 파일 인덱스 리스트 */
	private List<Long> fileIdxs;

}
