package com.example.demo.commute;

import com.example.demo.board.BoardDto;
import com.example.demo.dataroom.Dataroom;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/commute")
public class CommuteController {

	@Autowired
	private CommuteService cservice;
	@Autowired
	private MemberService mservice;

	// 자바에서 script 사용하기
	public static void init(HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
	}

	// 근태관리 목록
	@GetMapping("")
	public Map list() {
		ArrayList<CommuteDto> list = cservice.getAll();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		Map map = new HashMap();
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}

	// 나의 출퇴근
	@GetMapping("/mycommute")
	public Map mycommute() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		ArrayList<CommuteDto> list = cservice.getMyList(mdto.getId());
		Map map = new HashMap();
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}

	// 출퇴근기록 수정요청폼
	@GetMapping("/edit/{num}")
	public Map editRequest(@PathVariable("num") int num) {
		CommuteDto cdto = cservice.get(num);
		System.out.println(cdto);
		Map map = new HashMap();
		map.put("dto", cdto);
		return map;
	}

	// 출퇴근 수정저장
	@PostMapping("/edit")
	public Map edit(int num, String reason, String editStartTime, String editEndTime, String editBasicDate) {

		CommuteDto cdto = cservice.get(num);
		cdto.setEditBasicDate(editBasicDate);
		cdto.setReason(reason);
		cdto.setEditStartTime(editStartTime);
		cdto.setEditEndTime(editEndTime);
		cservice.save(cdto);
		ArrayList<CommuteDto> list = new ArrayList<>();
		list.add(cdto);
		Map map = new HashMap();
		map.put("list", list);
		System.out.println("근태수정 신청이 완료됬습니다.");
		return map;
	}

	// 출퇴근기록 수정요청목록
	@GetMapping("/editlist")
	public Map edit(String editStartTime, String editEndTime) {

		ArrayList<CommuteDto> list = cservice.getEditRequestList(editStartTime, editEndTime);
		System.out.println(list);
		Map map = new HashMap();
		map.put("list", list);
		return map;
	}

	// 출근
	@PostMapping("/attendance")
	public Map attendance(CommuteDto cdto) {

		boolean flag = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
		String formattedTime1 = LocalDateTime.now().format(formatter1);
		String formattedTime2 = LocalDateTime.now().format(formatter2);
		CommuteDto cdto2 = cservice.getByDateAndUserName(formattedTime1, mdto.getUsername());
		System.out.println("mdto.getCstatus() = " + mdto.getCstatus());
		System.out.println(cdto2);
		if (cdto2 == null) {
			mdto.setCstatus(1);
			mservice.save(mdto);
			cdto.setBasicDate(formattedTime1);
			cdto.setStartTime(formattedTime2);
			cdto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(),
					mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(),
					mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(),
					mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(),
					mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
			cservice.save(cdto);
			flag = true;
			System.out.println("출근이 정상적으로 처리되었습니다.");
		} else {
			System.out.println("이미 출근처리가 완료되었습니다.");
		}
		Map map = new HashMap();
		map.put("mdto", mdto);
		map.put("flag", flag);
		map.put("dto", cdto);
		return map;
	}

	// 퇴근
	@PostMapping("/quit")
	public Map quit(CommuteDto cdto) {

		boolean flag = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
		String formattedTime1 = LocalDateTime.now().format(formatter1);
		String formattedTime2 = LocalDateTime.now().format(formatter2);
		cdto = cservice.getByDateAndUserName(formattedTime1, mdto.getUsername());
		System.out.println(cdto.getEndTime());
		System.out.println(formattedTime2);
		System.out.println("mdto.getCstatus() = " + mdto.getCstatus());
		if (cdto.getEndTime() == null) {
			mdto.setCstatus(0);
			mservice.save(mdto);
			cdto.setEndTime(formattedTime2);
			System.out.println("test " + cdto.getNum());
			System.out.println("test " + cdto);
			cdto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(),
					mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(),
					mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(),
					mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(),
					mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
			cservice.save(cdto);
			System.out.println("출근이 정상적으로 처리되었습니다.");
			flag = true;
		} else {

			System.out.println("이미 퇴근처리가 완료되었습니다.");
		}
		Map map = new HashMap();
		map.put("mdto", mdto);
		map.put("flag", flag);
		map.put("dto", cdto);
		return map;
	}

	// 수정요청 승인
	@PostMapping("/approve")
	public Map approve(int num) {
		boolean flag = false;
		CommuteDto cdto = cservice.get(num);
		cdto.setStartTime(cdto.getEditStartTime());
		cdto.setEndTime(cdto.getEditEndTime());
		cdto.setBasicDate(cdto.getEditBasicDate());
		cdto.setEditStartTime("");
		cdto.setEditEndTime("");
		cdto.setEditBasicDate("");
		cdto.setReason("");
		cservice.save(cdto);
		flag = true;
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

	// 수정요청 거절
	@PostMapping("/cancel")
	public Map cancel(int num) {
		boolean flag = false;
		CommuteDto cdto = cservice.get(num);
		cdto.setEditBasicDate("");
		cdto.setEditStartTime("");
		cdto.setEditEndTime("");
		cdto.setReason("");
		cservice.save(cdto);
		flag = true;
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

	// 옵션으로 검색(전체 출퇴근 관리)
	/*
	 * @GetMapping("/search") public String getByOption(String type, Model map,
	 * String option, @RequestParam(value = "page", defaultValue = "1") int page) {
	 * Page<CommuteDto> list2 = cservice.getByOption(type, option, page - 1);
	 * map.addAttribute("paging", list2); map.addAttribute("type", type);
	 * map.addAttribute("option", option); return "commute/list"; }
	 * 
	 * 
	 * 
	 * //옵션으로 검색(내 출퇴근 이력)
	 * 
	 * @GetMapping("/searchMyCommute") public String getByMycommute(String type,
	 * Model map, String option, @RequestParam(value = "page", defaultValue = "1")
	 * int page) { Authentication authentication =
	 * SecurityContextHolder.getContext().getAuthentication(); Member loginMember =
	 * (Member) authentication.getPrincipal();
	 * 
	 * Page<CommuteDto> list = cservice.getByOptionAndMember(type, option,
	 * loginMember.getId(), page - 1); map.addAttribute("paging", list);
	 * map.addAttribute("type", type); map.addAttribute("option", option); return
	 * "commute/mycommute"; }
	 * 
	 * 
	 * //출퇴근기록 수정요청 저장
	 * 
	 * @PostMapping("/editRequest") public void edit(HttpServletResponse response,
	 * Model map, int commuteNum, String reason, String editStartTime, String
	 * editEndTime, String editBasicDate) { try { CommuteDto cdto =
	 * cservice.get(commuteNum); cdto.setEditBasicDate(editBasicDate);
	 * cdto.setReason(reason); cdto.setEditStartTime(editStartTime);
	 * cdto.setEditEndTime(editEndTime); cservice.save(cdto); ArrayList<CommuteDto>
	 * list = new ArrayList<>(); list.add(cdto); map.addAttribute("list", list);
	 * init(response); PrintWriter out = response.getWriter(); out.println(String.
	 * format("<script>alert('근태수정 신청이 완료됐습니다.'); location.href='/main';</script>"))
	 * ; out.flush(); } catch (IOException e) { throw new RuntimeException(e); } }
	 * 
	 * //출퇴근기록 수정요청목록
	 * 
	 * @GetMapping("/editRequestList") public String edit(Model
	 * map, @RequestParam(value = "page", defaultValue = "1") int page, String
	 * editStartTime, String editEndTime) { Page<Commute> paging =
	 * this.cservice.getEditRequestList(page - 1, editStartTime, editEndTime);
	 * map.addAttribute("paging", paging); return "commute/editRequestList"; }
	 * 
	 * //수정요청 승인
	 * 
	 * @RequestMapping("/approve") public String approve(int commuteNum) {
	 * CommuteDto cdto = cservice.get(commuteNum);
	 * cdto.setStartTime(cdto.getEditStartTime());
	 * cdto.setEndTime(cdto.getEditEndTime());
	 * cdto.setBasicDate(cdto.getEditBasicDate()); cdto.setEditStartTime("");
	 * cdto.setEditEndTime(""); cdto.setEditBasicDate(""); cdto.setReason("");
	 * cservice.save(cdto); return "redirect:/commute/editRequestList"; }
	 * 
	 * //수정요청 거절
	 * 
	 * @RequestMapping("/cancel") public String cancel(int commuteNum) { CommuteDto
	 * cdto = cservice.get(commuteNum); cdto.setEditBasicDate("");
	 * cdto.setEditStartTime(""); cdto.setEditEndTime(""); cdto.setReason("");
	 * cservice.save(cdto); return "redirect:/commute/editRequestList"; }
	 * 
	 */
}