package com.example.demo.member;

import com.example.demo.auth.JwtTokenProvider;
import com.example.demo.board.BoardDto;
import com.example.demo.member.dto.MemberJoinDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController // rest api controller
@CrossOrigin(origins = "*") // 모든 ip로부터 요청 받기 허용
public class MemberController {
    @Autowired
    private MemberService service;

    @Autowired
    private JwtTokenProvider tokenprovider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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
    @PostMapping("/join")
    public Map join(@RequestBody MemberJoinDto memberJoinDto) {
        MemberDto res = service.create(memberJoinDto);
        Map map = new HashMap();
        map.put("name", res.getName());
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

    @GetMapping("/edit/{username}")
    public Map editForm(String username) {
        Map map = new HashMap();
        MemberDto dto = service.getMember(username);
        map.put("m", dto);
        return map;
    }

    @PutMapping("/edit")
    public Map edit(@RequestBody MemberDto dto) {
        Map map = new HashMap();
        System.out.println("dto.getDeptCode() = " + dto.getDeptCode());
        System.out.println("dto.getDeptName() = " + dto.getDeptName());
        System.out.println("dto.getPwd() = " + dto.getPwd());

        MemberDto m = service.getMemberByName(dto.getName());
        m.setUsername(dto.getUsername());
        m.setPwd(dto.getPwd());
        m.setName(dto.getName());
        m.setCompanyName(dto.getCompanyName());
        m.setDeptCode(dto.getDeptCode());
        m.setCompanyRank(dto.getCompanyRank());
        m.setNewNo(dto.getNewNo());
        m.setEmail(dto.getEmail());
        m.setAddress(dto.getAddress());
        m.setComCall(dto.getComCall());
        m.setPhone(dto.getPhone());
        m.setIsMaster(dto.getIsMaster());
        m.setStatus(dto.getStatus());
        service.save(m);
        ArrayList<MemberDto> list = service.getAll();
        map.put('m', m);
        map.put("list", list);
        return map;
    }

    @Transactional
    @PostMapping("/del")
    public Map delete(String username) {
        System.out.println("username = " + username);
        service.delete(username);
        Map map = new HashMap();
        ArrayList<MemberDto> list = service.getAll();
        map.put("list", list);
        return map;
    }
}
