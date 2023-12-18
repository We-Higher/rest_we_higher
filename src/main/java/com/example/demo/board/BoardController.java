package com.example.demo.board;

import com.example.demo.auth.SecurityMember;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map list(@RequestParam(value = "page", defaultValue = "1") int page) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);

		Page<Board> paging = this.bservice.getBoardList(page - 1);
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
	
	// 글작성 폼
	@GetMapping("/add")
	public Map BoardAdd() {
		Map map = new HashMap();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
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
	
    // 옵션으로 검색
    @GetMapping("/search")
    public Map getbyOption(String type, String option) {
		Map map = new HashMap();
        System.out.println(type);
        System.out.println(option);
        List<Board> list = bservice.getByOption2(type, option);
		map.put("list", list);
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
		bservice.editCnt(num);
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
