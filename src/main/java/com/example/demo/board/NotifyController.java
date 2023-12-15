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
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/notify")
public class NotifyController {
	@Autowired
	private BoardService bservice;

	@Autowired
	private MemberService mservice;

	//공지사항 목록
	@GetMapping("list")
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

}
