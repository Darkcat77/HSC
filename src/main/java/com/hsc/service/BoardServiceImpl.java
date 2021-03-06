package com.hsc.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hsc.domain.AttachDTO;
import com.hsc.domain.BoardDTO;
import com.hsc.mapper.AttachMapper;
import com.hsc.mapper.BoardMapper;
import com.hsc.mapper.BoardViewMapper;
import com.hsc.paging.PaginationInfo;
import com.hsc.util.FileUtils;

@Service
@PropertySource("classpath:Resource.properties")
public class BoardServiceImpl implements BoardService {
	
	@Value("${evtResource.path}")
	private String evtPath;
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private BoardViewMapper boardViewMapper;
	
	@Autowired
	private AttachMapper attachMapper;

	@Autowired
	private FileUtils fileUtils;	

	@Override
	public boolean registerBoard(BoardDTO params) {
		int queryResult = 0;

		if (params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else {
			queryResult = boardMapper.updateBoard(params);
			// 파일이 추가, 삭제, 변경된 경우
			if ("Y".equals(params.getChangeYn())) {
				attachMapper.deleteAttach(params.getIdx());

				// fileIdxs에 포함된 idx를 가지는 파일의 삭제여부를 'N'으로 업데이트
				if (CollectionUtils.isEmpty(params.getFileIdxs()) == false) {
					attachMapper.undeleteAttach(params.getFileIdxs());
				}
			}
		}
		return (queryResult > 0);
	}
	
	@Override
	public boolean registerBoard(BoardDTO params, MultipartFile[] files) {
		int queryResult = 1;

		if (registerBoard(params) == false) {
			return false;
		}

		List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx(), evtPath);
		if (CollectionUtils.isEmpty(fileList) == false) {
			queryResult = attachMapper.insertAttach(fileList);
			if (queryResult < 1) {
				queryResult = 0;
			}
		}

		return (queryResult > 0);
	}
	
	@Override
	public boolean registerEventBoard(BoardDTO params, MultipartFile[] files) {
		
		int queryResult = 0;
		if ("Y".equals(params.getChangeYn())) {
			List<AttachDTO> fileList = fileUtils.uploadFiles(files, params.getIdx(), evtPath);
			if (CollectionUtils.isEmpty(fileList) == false) {
				for(AttachDTO attachDTO : fileList) {
					if (params.getOrgImg().toString().equals(attachDTO.getOriginalName().toString())){
						params.setSavImg(attachDTO.getSaveName());
					} else if (params.getOrgQrImg().toString().equals(attachDTO.getOriginalName().toString())){
						params.setSavQrImg(attachDTO.getSaveName());
					} else if (params.getOrgEnQrImg().toString().equals(attachDTO.getOriginalName().toString())){
						params.setSavEnQrImg(attachDTO.getSaveName());
					}
				}			
			}
		}

		if (params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else {
			queryResult = boardMapper.updateBoard(params);
		}
		return (queryResult > 0);
	}

	@Override
	public BoardDTO getBoardDetail(Long idx) {
		return boardMapper.selectBoardDetail(idx);
	}

	@Override
	public boolean deleteBoard(Long idx) {
		int queryResult = 0;

		BoardDTO board = boardMapper.selectBoardDetail(idx);

		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);
        
		params.setPaginationInfo(paginationInfo);
		
		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}
	
	@Override
	public List<BoardDTO> getBoardViewList(BoardDTO params) {
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardViewMapper.selectBoardTotalCount(params);
		
		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);
        
		params.setPaginationInfo(paginationInfo);
		
		if (boardTotalCount > 0) {
			boardList = boardViewMapper.selectBoardList(params);
		}

		return boardList;
	}	
	
	@Override
	public List<AttachDTO> getAttachFileList(Long boardIdx) {

		int fileTotalCount = attachMapper.selectAttachTotalCount(boardIdx);
		if (fileTotalCount < 1) {
			return Collections.emptyList();
		}
		return attachMapper.selectAttachList(boardIdx);
	}
	
	@Override
	public AttachDTO getAttachDetail(Long idx) {
		return attachMapper.selectAttachDetail(idx);
	}
    
}
