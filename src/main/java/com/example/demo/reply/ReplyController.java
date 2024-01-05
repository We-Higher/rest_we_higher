package com.example.demo.reply;

import com.example.demo.board.Board;
import com.example.demo.board.BoardDto;
import com.example.demo.board.BoardService;
import com.example.demo.member.Member;
import com.example.demo.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.member.MemberDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/reply")
public class ReplyController {

	@Autowired
	private BoardService bservice;
	@Autowired
	private MemberService mservice;
	@Autowired
	private ReplyService rservice;

	@GetMapping("/list/{com_bno}")
	@ResponseBody
	public Map getList(@PathVariable int com_bno) {
		System.out.println("댓글 목록 컨트롤러 동작");
		System.out.println(com_bno);
		List<Reply> list = rservice.getList(com_bno);
		int total = rservice.getTotal(com_bno);
		System.out.println("댓글 갯수 " + rservice.getTotal(com_bno));
		Map map = new HashMap();
		map.put("list", list);
		map.put("total", total);
		return map;
	}

	// 댓글 등록
	@PostMapping("/add")
	@ResponseBody
	public Map addReply(@RequestBody List<Map<String, Object>> param) {

		Map map = new HashMap();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member user = (Member) authentication.getPrincipal();
		MemberDto mdto = new MemberDto().toDto(user);
		Member member = new Member().toEntity(mdto);
		int num = Integer.parseInt((String) param.get(0).get("com_bno"));
		String content = (String) param.get(0).get("com_content");
		Board board = bservice.getBoard2(num);
		System.out.println("num" + num);
		System.out.println("content" + content);
		ReplyDto dto = ReplyDto.builder().wdate(null).udate(null).board(board).title("").content(content).member(member)
				.build();
		ReplyDto r = rservice.saveReply(dto);
		map.put("dto", r);
		return map;
	}
	
	// 댓글 삭제
	@PostMapping("/del")
	public Map del(int num) {

		Map map = new HashMap();
		rservice.delCommute(num);
		ArrayList<BoardDto> list = bservice.getAll();
		map.put("num", num);
		map.put("list", list);
		return map;
	}

}
