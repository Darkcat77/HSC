package com.hsc.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.hsc.constant.Method;
import com.hsc.domain.BoardDTO;
import com.hsc.domain.TouritemDTO;
import com.hsc.service.BoardService;
import com.hsc.service.TourService;
import com.hsc.util.UiUtils;




@Controller
@PropertySource("classpath:Resource.properties")
public class BoardController extends UiUtils{
	
	@Value("${resource.path}")
	private String resourcePath;

	@Value("${tourResource.path}")
	private String tourPath;
	
	@Value("${evtResource.path}")
	private String evtPath;
	
	@Value("${qrResource.path}")
	private String qrPath;
	@Value("${mvResource.path}")
	private String mvPath;

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private TourService tourService;
	

	
	
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
	

	
	
	
	
	

	

		
}
