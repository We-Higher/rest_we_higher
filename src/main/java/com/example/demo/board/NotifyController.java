package com.example.demo.board;

import com.example.demo.auth.SecurityMember;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/notify")
public class NotifyController {
	@Autowired
	private BoardService bservice;

	@Autowired
	private MemberService mservice;

	//공지사항 목록
	@GetMapping("/list")
	public Map Notifylist() {
		ArrayList<NotifyDto> list = bservice.getAllnotify();
		Map map = new HashMap();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}

	//공지사항 목록
	@GetMapping("")
	public Map list(@RequestParam(value = "page", defaultValue = "1") int page) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);

		Page<Notify> paging = this.bservice.getNotifyList(page - 1);
		Map map = new HashMap();

		map.put("mdto", mdto);
		map.put("currentPage", page);  // 현재 페이지 번호
		map.put("hasNext", paging.hasNext());  // 다음 페이지가 있는지 여부
		map.put("hasPrevious", paging.hasPrevious());  // 이전 페이지가 있는지 여부
		map.put("totalPages", paging.getTotalPages());  // 전체 페이지 수
		map.put("list", paging.getContent());  // 현재 페이지의 내용
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date = LocalDateTime.now().format(formatter1);
		map.put("date", date);
		return map;
	}
	
    // 옵션으로 검색
	@GetMapping("/search")
	public Map getbyOption2(String type, String option, @RequestParam(value = "page", defaultValue = "1") int page) {
		Map map = new HashMap();
		System.out.println(type);
		System.out.println(option);

		Page<NotifyDto> paging = bservice.getByOption2(type, option, page - 1);
		map.put("currentPage", page);  // 현재 페이지 번호
		map.put("hasNext", paging.hasNext());  // 다음 페이지가 있는지 여부
		map.put("hasPrevious", paging.hasPrevious());  // 이전 페이지가 있는지 여부
		map.put("totalPages", paging.getTotalPages());  // 전체 페이지 수
		map.put("list", paging.getContent());  // 현재 페이지의 내용
		return map;
	}
	
	//공지사항 작성폼
	@GetMapping("/add")
	public Map NotifyAdd() {
		ArrayList<NotifyDto> list = bservice.getAllnotify();
		Map map = new HashMap();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}
	
	//공지사항 추가
	@PostMapping("/add")
	public Map NotifyAdd2(NotifyDto b) {

        int check = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityMember member = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = mservice.getMember(member.getUsername());
        b.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
        NotifyDto d = bservice.saveNotify(b, check);
		Map map = new HashMap();
		map.put("dto", d);
		return map;
	}
	
	// 공지사항 상세페이지
	@RequestMapping("/{num}")
	public Map get(@PathVariable("num") int num) {

		Map map = new HashMap();
		NotifyDto b = bservice.getNotify(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto m = mservice.getMember(id);
		bservice.editCnt2(num);
		map.put("m,", m);
		map.put("dto", b);
		return map;
	}
	
	// 수정폼
    @GetMapping("/edit/{num}")
    public Map editForm(@PathVariable("num") int num) {
    	
		Map map = new HashMap();
		NotifyDto b = bservice.getNotify(num);
        map.put("dto", b);
		return map;
    }
    
	// 공지사항 수정
	@PutMapping("/edit")
	public Map edit(BoardDto b) {
		
		int check = 1;
		NotifyDto b2 = bservice.getNotify(b.getNum());
		b2.setTitle(b.getTitle());
		b2.setContent(b.getContent());
		NotifyDto d = bservice.saveNotify(b2, check);
		Map map = new HashMap();
		map.put("dto", d);
		return map;
	}
	
	// 삭제
	@PostMapping("/del")
	public Map del(int num) {
		bservice.delNotify(num);
		Map map = new HashMap();
		ArrayList<NotifyDto> list = bservice.getAllnotify();
		map.put("num", num);
		map.put("list", list);
		return map;
	}

}
