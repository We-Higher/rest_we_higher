package com.example.demo.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.auth.SecurityMember;
import com.example.demo.member.dto.MemberJoinDto;

@RestController // rest api controller
@CrossOrigin(origins = "*") // 모든 ip로부터 요청 받기 허용
public class MemberController {
	@Autowired
	private MemberService service;

	@Autowired
	private JwtTokenProvider tokenprovider;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@PostMapping("/join")
	public Map join(MemberJoinDto memberJoinDto) {
		MemberDto res = service.create(memberJoinDto);
		Map map = new HashMap();
		map.put("username", res.getUsername());
		return map;
	}

	@PostMapping("/login")
	public Map login(String username, String password) {
		System.out.println(username + " , " + password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		System.out.println(authenticationToken);
	    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
	    System.out.println(authentication.isAuthenticated());
	    
	    Map map = new HashMap();
		boolean flag = authentication.isAuthenticated();
		System.out.println(flag);
		if (flag) {
			String token = tokenprovider.generateJwtToken(new MemberDto(null, username, "","","", "", "", "", 0, "", 0, "", "", "", 0, 0, 0, "", "", 0, 0, 0, null));
			flag = true;
			map.put("token", token);
		}
		map.put("flag", flag);
		return map;
	}
	
	@GetMapping("/auth/mypage")
	public Map mypage() {
		Map map = new HashMap();
		boolean flag = true;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();//로그인 아이디 추출
		MemberDto mdto = service.getMember(id);
		System.out.println(mdto);
		if (mdto == null) {
			flag = false;
		} else {
			map.put("mdto", mdto);
		}
		map.put("flag", flag);
		return map;
	}

	@GetMapping("/auth/info")
	public Map myinfo() {
		Map map = new HashMap();
		boolean flag = true;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();//로그인 아이디 추출
		MemberDto m = service.getMember(id);
		System.out.println(m);
		if (m == null) {
			flag = false;
		} else {
			map.put("m", m);
		}
		map.put("flag", flag);
		return map;
	}

	
}
