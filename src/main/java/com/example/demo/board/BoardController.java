package com.example.demo.board;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.SecurityMember;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/board")
public class BoardController {

	@Autowired
	private BoardService bservice;

	@Autowired
	private MemberService mservice;
	
	// 글목록
	@GetMapping("")
	public Map list() {
		ArrayList<BoardDto> list = bservice.getAll();
		Map map = new HashMap();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("list", list);
		return map;
	}
		
	// 글작성
	@PostMapping("")
	public Map add(BoardDto b) {

        int check = 0;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		SecurityMember member = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = mservice.getMember(member.getUsername());
        b.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
		BoardDto d = bservice.saveBoard(b, check);
		Map map = new HashMap();
		map.put("dto", d);
		return map;
	}

	// 글 상세페이지
	@RequestMapping("/{num}")
	public Map get(@PathVariable("num") int num) {

		Map map = new HashMap();
		BoardDto b = bservice.getBoard(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto m = mservice.getMember(id);
		map.put("m,", m);
		map.put("dto", b);
		return map;
	}
	
	// 수정폼
    @GetMapping("/edit/{num}")
    public Map editForm(@PathVariable("num") int num) {
    	
		Map map = new HashMap();
        BoardDto b = bservice.getBoard(num);
        map.put("dto", b);
		return map;
    }

	// 수정
	@PutMapping("/edit")
	public Map edit(BoardDto b) {
		
		int check = 1;
		BoardDto b2 = bservice.getBoard(b.getNum());
		b2.setTitle(b.getTitle());
		b2.setContent(b.getContent());
		BoardDto d = bservice.saveBoard(b2, check);
		Map map = new HashMap();
		map.put("dto", d);
		return map;
	}

	// 삭제
	@PostMapping("/del")
	public Map del(int num) {
		bservice.delBoard(num);
		Map map = new HashMap();
		ArrayList<BoardDto> list = bservice.getAll();
		map.put("num", num);
		map.put("list", list);
		return map;
	}
	
    /*@GetMapping("")
    public Map list(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Board> paging = this.bservice.getBoardList(page - 1);
        Map map = new HashMap();
        map.put("paging", paging);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter1);
        map.put("date", date);
        return map;
    }*/
	
	// 글목록
    /*@GetMapping("")
    public Map list(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Board> paging = this.bservice.getBoardList(page - 1);
		Map map = new HashMap();
        ((Model) map).addAttribute("paging", paging);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter1);
        ((Model) map).addAttribute("date", date);
		map.put("list", paging);
		return map;
    }*/
}
