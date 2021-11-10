package com.hsc.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hsc.constant.Method;
import com.hsc.domain.AttachDTO;
import com.hsc.domain.BoardDTO;
import com.hsc.domain.TerminalDTO;
import com.hsc.domain.TouritemDTO;
import com.hsc.service.BoardService;
import com.hsc.service.TourService;
import com.hsc.service.TerminalService;
import com.hsc.util.UiUtils;




@Controller
@PropertySource("classpath:Resource.properties")
public class BoardController extends UiUtils{
	


	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TourService tourService;
	
	@Autowired
	private TerminalService terminalService;
	

	@GetMapping(value = "/board/write_terminal.do")
	public String openBoardTerminalWrite(@ModelAttribute("params") TerminalDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			model.addAttribute("terminal", new TerminalDTO());
		} else {
			TerminalDTO terminal = terminalService.getTermialDetail(idx);
			
			if (terminal == null || "Y".equals(terminal.getDeleteYn())) {
				return showMessageWithRedirect("없는 터미널이거나 이미 삭제된 터미널입니다.", "/board/list.do", Method.GET, null, model);
			}
			
			//파일리스트 가져오기
			List<AttachDTO> fileList = terminalService.getAttachFileList(idx);
			model.addAttribute("fileList", fileList);
			
			model.addAttribute("terminal", terminal);
		}

		model.addAttribute("page", "terminal");
		return "board/write_terminal";
	}
	
	@GetMapping(value = "/board/write_event.do")
	public String openBoardEventWrite(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			model.addAttribute("board", new BoardDTO());
		} else {
			BoardDTO board = boardService.getBoardDetail(idx);
			
			if (board == null || "Y".equals(board.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
			}
			model.addAttribute("board", board);
			model.addAttribute("page", "event");

		}

		return "board/write_event";
	}
	
	@PostMapping(value = "/board/register.do")
	//public String registerBoard(@ModelAttribute("params") final BoardDTO params,  Model model) {
	public String registerBoard(final BoardDTO params, final MultipartFile[] files, Model model) { //파일처리 추가
		Map<String, Object> pagingParams = getPagingParams(params);
		try {
			//boolean isRegistered = boardService.registerBoard(params);
			boolean isRegistered = boardService.registerBoard(params, files); //파일처리 추가
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
    
		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list.do", Method.GET, pagingParams, model);
		}
        
		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list.do", Method.GET, pagingParams, model);
		//return "redirect:/board/list.do";
	}
	
	@PostMapping(value = "/board/registerevent.do")
	public String registerEventBoard(final BoardDTO params, final MultipartFile[] files, Model model) { //파일처리 추가
		Map<String, Object> pagingParams = getPagingParams(params);
		model.addAttribute("page", "event");
		try {
			boolean isRegistered = boardService.registerEventBoard(params, files); //파일처리 추가
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
    
		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
		}
        
		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
	}
	
	@PostMapping(value = "/board/registertermianl.do")
	public String registerTerminal(final TerminalDTO params, final MultipartFile[] files, Model model) { //파일처리 추가
		Map<String, Object> pagingParams = getPagingParams(params);
		model.addAttribute("page", "event");
		try {
			boolean isRegistered = terminalService.registerTermial(params, files); //파일처리 추가
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
    
		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
		}
        
		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
	}
	
	@GetMapping(value = "/board/list.do")
	public String openBoardList(@ModelAttribute("params") BoardDTO params, Model model) {
		List<BoardDTO> boardList = boardService.getBoardList(params);
	    model.addAttribute("boardList", boardList);

		return "board/list";
	}
	
	@GetMapping(value = "/board/list_event.do")
	public String openBoardList2(@ModelAttribute("params") BoardDTO params, Model model) {
		List<BoardDTO> boardList = boardService.getBoardList(params);
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("page", "event");

		return "board/list_event";
	}
	
	@GetMapping(value = "/board/list_terminal.do")
	public String openTerminalList(@ModelAttribute("params") TerminalDTO params, Model model) {
		List<TerminalDTO> terminalList = terminalService.getTermialList(params);
	    model.addAttribute("terminalList", terminalList);
	    model.addAttribute("page", "terminal");

		return "board/list_terminal";
	}
	
	@GetMapping(value = "/board/index.do")
	public String openViewBoardList(@ModelAttribute("params") BoardDTO params, 
			@RequestParam(value = "lang", required = false) String lang, 
			@RequestParam(value = "kiosk", required = false) String kiosk,
			HttpSession session,
			Model model) {
		//추후 기간내 검색 추가
		session.setAttribute("kiosk", kiosk);
		session.setMaxInactiveInterval(24*60*60);
		List<BoardDTO> boardList = boardService.getBoardViewList(params);
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("lang", lang);
		return "board/index";
	}
	
	@GetMapping(value = "/board/todo_sel_history.do")
	public String openTourBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("T");
		params.setCategory(Long.valueOf(0));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/todo_sel_history";
	}

	@GetMapping(value = "/board/todo_sel_museum.do")
	public String openMuseumBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("M");
		params.setCategory(Long.valueOf(0));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/todo_sel_museum";
	}
	
	@GetMapping(value = "/board/todo_sel_park.do")
	public String openParkBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("P");
		params.setCategory(Long.valueOf(0));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/todo_sel_park";
	}
	
	@GetMapping(value = "/board/todo_sel_show.do")
	public String openShowBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("E");
		params.setCategory(Long.valueOf(0));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/todo_sel_show";
	}
	
	
	@GetMapping(value = "/board/todo_place.do")
	public String openPlace(@RequestParam(value = "lang", required = false) String lang, Model model) {
		model.addAttribute("lang", lang);
		return "board/todo_place";
	}
	
	@GetMapping(value = "/board/landmark.do")
	public String openLandmark(@RequestParam(value = "lang", required = false) String lang, Model model) {
		model.addAttribute("lang", lang);
		return "board/landmark";
	}
	
//	@GetMapping(value = "/board/gcc.do")
//	public String openGcc(@RequestParam(value = "lang", required = false) String lang, Model model) {
//		model.addAttribute("lang", lang);
//		return "board/gcc";
//	}
	
	@GetMapping(value = "/board/food_kr.do")
	public String openKrFoodBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("F");
		params.setCategory(Long.valueOf(1));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/food_kr";
	}
	
	@GetMapping(value = "/board/food_ch.do")
	public String openChFoodBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("F");
		params.setCategory(Long.valueOf(2));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/food_ch";
	}
	
	@GetMapping(value = "/board/food_jp.do")
	public String openJpFoodBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("F");
		params.setCategory(Long.valueOf(3));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/food_jp";
	}
	
	@GetMapping(value = "/board/food_ws.do")
	public String openWsFoodBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("F");
		params.setCategory(Long.valueOf(4));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/food_ws";
	}
	
	@GetMapping(value = "/board/food_dessert.do")
	public String openDessertFoodBoardList(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "lang", required = false) String lang, Model model) {
		params.setCType("F");
		params.setCategory(Long.valueOf(5));
		List<TouritemDTO> tourList = tourService.getTourViewList(params);
	    model.addAttribute("tourList", tourList);
	    model.addAttribute("lang", lang);

		return "board/food_dessert";
	}
	
	
	@GetMapping(value = "/board/view.do")
	public String openBoardDetail(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if (board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		
		List<AttachDTO> fileList = boardService.getAttachFileList(idx); 
		model.addAttribute("fileList", fileList); 

		return "board/view";
	}
	
	@GetMapping(value = "/board/todo_place_food.do")
	public String openFood(@RequestParam(value = "lang", required = false) String lang, Model model) {
		model.addAttribute("lang", lang);
		return "board/todo_place_food";
	}
	
	@GetMapping(value = "/board/k_airport.do")
	public String openKairport(@RequestParam(value = "lang", required = false) String lang, @RequestParam(value = "airport", required = false) String airport, Model model) {
		model.addAttribute("lang", lang);
		model.addAttribute("airport", airport);
		return "board/k_airport";
	}
	
	@GetMapping(value = "/board/k_sub.do")
	public String openKsub(@RequestParam(value = "lang", required = false) String lang, Model model) {
		model.addAttribute("lang", lang);
		return "board/k_sub";
	}
	
	@GetMapping(value = "/board/trafic.do")
	public String openTrafic(@RequestParam(value = "lang", required = false) String lang, Model model) {
		model.addAttribute("lang", lang);
		return "board/trafic";
	}
	
	@GetMapping(value = "/board/eventview.do")
	public String _openEventBoardDetail(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if (board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		
		
		return "board/eventview";
	}
	
	@GetMapping(value = "/board/view_event.do")
	public String openEventBoardDetail(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		BoardDTO board = boardService.getBoardDetail(idx);
		if (board == null || "Y".equals(board.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("board", board);
		model.addAttribute("page", "event");
		
		
		return "board/view_event";
	}
	
	@GetMapping(value = "/board/view_terminal.do")
	public String openTerminalDetail(@ModelAttribute("params") TerminalDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		TerminalDTO terminal = terminalService.getTermialDetail(idx);
		if (terminal == null || "Y".equals(terminal.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("terminal", terminal);
		
		List<AttachDTO> fileList = boardService.getAttachFileList(idx); 
		model.addAttribute("fileList", fileList); 
		
		model.addAttribute("page", "terminal");
		
		
		return "board/view_terminal";
	}
	
	
	@GetMapping(value = "/board/touritemview.do")
	public String openTourItemDetail(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list.do", Method.GET, null, model);
		}

		TouritemDTO tourItem = tourService.getTourItemDetail(idx);
		
		if (tourItem == null || "Y".equals(tourItem.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/list.do", Method.GET, null, model);
		}
		model.addAttribute("tourItem", tourItem);
		
		return "board/touritemview";
	}
	
	
	@GetMapping(value = "/board/todo_detail.do")
	public String openTourViewItemDetail(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "idx", required = false) Long idx, 
			@RequestParam(value = "lang", required = false) String lang, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/todo_sel_history.do", Method.GET, null, model);
		}

		TouritemDTO tourItem = tourService.getTourItemDetail(idx);
		tourService.registerHit(tourItem);
		if (tourItem == null || "Y".equals(tourItem.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/todo_sel_history.do", Method.GET, null, model);
		}
		model.addAttribute("tourItem", tourItem);
		model.addAttribute("lang", lang);
		
		return "board/todo_detail";
	}
	
	@GetMapping(value = "/board/todo_detail_food.do")
	public String openFoodViewItemDetail(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "idx", required = false) Long idx, 
			@RequestParam(value = "lang", required = false) String lang, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/todo_sel_history.do", Method.GET, null, model);
		}

		TouritemDTO tourItem = tourService.getTourItemDetail(idx);
		tourService.registerHit(tourItem);
		if (tourItem == null || "Y".equals(tourItem.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/todo_sel_history.do", Method.GET, null, model);
		}
		model.addAttribute("tourItem", tourItem);
		model.addAttribute("lang", lang);
		
		return "board/todo_detail_food";
	}	
	
	@PostMapping(value = "/board/delete.do")
	public String deleteBoard(@ModelAttribute("params") BoardDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list_event.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		
		try {
			boolean isDeleted = boardService.deleteBoard(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list_event.do", Method.GET, pagingParams, model);
	}

	@PostMapping(value = "/board/delete_terminal.do")
	public String deleteTerminal(@ModelAttribute("params") TerminalDTO params, @RequestParam(value = "idx", required = false) Long idx, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/list_terminal.do", Method.GET, null, model);
		}

		Map<String, Object> pagingParams = getPagingParams(params);
		
		try {
			boolean isDeleted = terminalService.deleteTermial(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/list_terminal.do", Method.GET, pagingParams, model);
	}
	
	
	@PostMapping(value = "/board/deleteTourItem.do")
	public String deleteTourItem(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "idx", required = false) Long idx
			, @RequestParam(value = "page", required = true) String page
			, @RequestParam(value = "cat_type", required = true) String cat_type
			, @RequestParam(value = "category", required = true) long category
			, Model model) {

		Map<String, Object> pagingParams = getPagingParams(params);
		pagingParams.put("page", page);
		pagingParams.put("cat_type", cat_type);
		pagingParams.put("category", category);
		
		model.addAttribute("cat_type", cat_type);
		model.addAttribute("category", category);
		model.addAttribute("page", page);
		
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
		}
		
		try {
			boolean isDeleted = tourService.deleteTourItem(idx);
			if (isDeleted == false) {
				return showMessageWithRedirect("게시글 삭제에 실패하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);

		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
		}

		return showMessageWithRedirect("게시글 삭제가 완료되었습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
	}
	
	@GetMapping(value = "/board/admintourinfo.do")
	public String openAdminHistoryBoardList(@ModelAttribute("params") TouritemDTO params
				, @RequestParam(value = "page", required = true) String page
				, @RequestParam(value = "cat_type", required = true) String cat_type
				, @RequestParam(value = "category", required = true) long category
				, Model model) {
		
		params.setCType(cat_type);
		params.setCategory(category);
		model.addAttribute("cat_type", cat_type);
		model.addAttribute("category", category);
		model.addAttribute("page", page);			
		
		List<TouritemDTO> tourList = tourService.getTourItemList(params);
	    model.addAttribute("tourList", tourList);
		/* model.addAttribute("reqpage", page); */	
	    

		return "board/list_tour";
	}
	
	@GetMapping(value = "/board/admintouritemview.do")
	public String openAdminTourItemDetail(@ModelAttribute("params") TouritemDTO params, @RequestParam(value = "idx", required = false) Long idx
				, @RequestParam(value = "page", required = true) String page
				, @RequestParam(value = "cat_type", required = true) String cat_type
				, @RequestParam(value = "category", required = true) long category			
				, Model model) {
		if (idx == null) {
			return showMessageWithRedirect("올바르지 않은 접근입니다.", "/board/admintourinfo.do", Method.GET, null, model);
		}

		TouritemDTO tourItem = tourService.getTourItemDetail(idx);
		
		if (tourItem == null || "Y".equals(tourItem.getDeleteYn())) {
			return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/admintourinfo.do", Method.GET, null, model);
		}
		model.addAttribute("tourItem", tourItem);
		params.setCType(cat_type);
		params.setCategory(category);
		model.addAttribute("cat_type", cat_type);
		model.addAttribute("category", category);
		model.addAttribute("page", page);			
		
		return "board/view_touritem";
	}
	
	@GetMapping(value = "/board/write_touritem.do")
	public String openBoardWriteTouritem(@ModelAttribute("params") TouritemDTO params
			, @RequestParam(value = "idx", required = false) Long idx
			, @RequestParam(value = "page", required = true) String page
			, @RequestParam(value = "cat_type", required = true) String cat_type
			, @RequestParam(value = "category", required = true) long category
			, Model model) {
		
		if (idx == null) {
			model.addAttribute("tourItem", new TouritemDTO());
		} else {
			TouritemDTO tourItem = tourService.getTourItemDetail(idx);
			
			if (tourItem == null || "Y".equals(tourItem.getDeleteYn())) {
				return showMessageWithRedirect("없는 게시글이거나 이미 삭제된 게시글입니다.", "/board/admintourinfo.do?page=${page}&cat_type=${cat_type}&category=${category}", Method.GET, null, model);
			}
			model.addAttribute("tourItem", tourItem);
		}
		params.setCType(cat_type);
		params.setCategory(category);
		model.addAttribute("cat_type", cat_type);
		model.addAttribute("category", category);
		model.addAttribute("page", page);	

		return "board/write_touritem";
	}
	
	@PostMapping(value = "/board/registertouritem.do")
	public String registerTouritem(final TouritemDTO params
			, @RequestParam(value = "page", required = false) String page
			, final MultipartFile[] files, Model model) { //파일처리 추가
		
		
		Map<String, Object> pagingParams = getPagingParams(params);
		pagingParams.put("page", page);
		pagingParams.put("cat_type", params.getCType());
		pagingParams.put("category", params.getCategory());
		
		
		
		try {
			boolean isRegistered = tourService.registerTourItem(params, files); //파일처리 추가
			if (isRegistered == false) {
				return showMessageWithRedirect("게시글 등록에 실패하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
			}
		} catch (DataAccessException e) {
			return showMessageWithRedirect("데이터베이스 처리 과정에 문제가 발생하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
    
		} catch (Exception e) {
			return showMessageWithRedirect("시스템에 문제가 발생하였습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
		}
        
		return showMessageWithRedirect("게시글 등록이 완료되었습니다.", "/board/admintourinfo.do", Method.GET, pagingParams, model);
	}
	
	

	
	
	@GetMapping(value = "/board/testImage.do")
	public String openImage(@ModelAttribute("params") String params, Model model) {
		return "board/testImage";
	}
	
	@GetMapping(value = "/board/dashboard.do")
	public String openDashboard() {
		return "board/dashboard";
	}
	@GetMapping(value = "/board/unknown.do")
	public String openunKnown() {
		return "board/404";
	}
	

		
}
