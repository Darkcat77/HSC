package com.hsc.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hsc.domain.BoardDTO;
import com.hsc.mapper.BoardMapper;

import java.util.List;

import org.springframework.util.CollectionUtils;

@SpringBootTest
class MapperTests {

	@Autowired
	private BoardMapper boardMapper;

	@Test
	public void testOfInsert() {
		BoardDTO params = new BoardDTO();

		params.setEvtNm("4번 게시글 제목");
		params.setContents("4번 게시글 내용");
		params.setPlace("테스터");

		int result = boardMapper.insertBoard(params);
		System.out.println("결과는 " + result + "입니다.");
	}
	
	@Test
	public void testOfSelectDetail() {
		BoardDTO board = boardMapper.selectBoardDetail((long) 4);
		try {
			//String boardJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(board);
			//String boardJson = new ObjectMapper().writeValueAsString(board);
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			String boardJson = objectMapper.writeValueAsString(board);

			System.out.println("=========================");
			System.out.println(board.getEvtNm());
			System.out.println(boardJson);
			System.out.println("=========================");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOfUpdate() {
		BoardDTO params = new BoardDTO();
		params.setEvtNm("1번 게시글 제목을 수정합니다.");
		params.setContents("1번 게시글 내용을 수정합니다.");
		params.setIdx((long) 1);

		int result = boardMapper.updateBoard(params);
		if (result == 1) {
			BoardDTO board = boardMapper.selectBoardDetail((long) 1);
			try {
				String boardJson = new ObjectMapper().writeValueAsString(board);

				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testOfDelete() {
		int result = boardMapper.deleteBoard((long) 1);
		if (result == 1) {
			BoardDTO board = boardMapper.selectBoardDetail((long) 1);
			try {
				String boardJson = new ObjectMapper().writeValueAsString(board);

				System.out.println("=========================");
				System.out.println(boardJson);
				System.out.println("=========================");

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testMultipleInsert() {
		for (int i = 2; i <= 50; i++) {
			BoardDTO params = new BoardDTO();
			params.setEvtNm(i + "번 게시글 제목");
			params.setContents(i + "번 게시글 내용");
			boardMapper.insertBoard(params);
		}
	}
	
	@Test
	public void testSelectList() {
		BoardDTO agrs = null;
		int boardTotalCount = boardMapper.selectBoardTotalCount(agrs);
		if (boardTotalCount > 0) {
			List<BoardDTO> boardList = boardMapper.selectBoardList(agrs);
			if (CollectionUtils.isEmpty(boardList) == false) {
				for (BoardDTO board : boardList) {
					System.out.println("=========================");
					System.out.println(board.getEvtNm());
					System.out.println(board.getContents());
					System.out.println("=========================");
				}
			}
		}
	}
	
	

}