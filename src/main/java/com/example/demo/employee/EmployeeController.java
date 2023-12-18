package com.example.demo.employee;

import com.example.demo.board.BoardDto;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;
    @Autowired
    private MemberService mservice;
    
    // 임직원 목록
	@GetMapping("")
	public Map list(@RequestParam(value = "page", defaultValue = "1") int page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto mdto = mservice.getMember(id);

        Map map = new HashMap();
        Page<Member> paging = this.mservice.getList(page - 1);

        map.put("mdto", mdto);
        map.put("currentPage", page);  // 현재 페이지 번호
        map.put("hasNext", paging.hasNext());  // 다음 페이지가 있는지 여부
        map.put("hasPrevious", paging.hasPrevious());  // 이전 페이지가 있는지 여부
        map.put("totalPages", paging.getTotalPages());  // 전체 페이지 수
        map.put("list", paging.getContent());  // 현재 페이지의 내용
        return map;
    }
	
    // 옵션으로 검색
    @GetMapping("/search")
    public Map getbyOption(String type, String option) {
		Map map = new HashMap();
        System.out.println(type);
        System.out.println(option);
        List<Member> list = service.getByOption2(type, option);
		map.put("list", list);
		return map;
    }
    
    // 임직원 목록
    /*@GetMapping("/list")
    public String list(Model map, @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Member> paging = this.mservice.getList(page - 1);
        map.addAttribute("paging", paging);
        ArrayList<MemberDto> list = mservice.getAll();
        map.addAttribute("list", list);
        return "employee/list";
    }*/

    // 옵션으로 검색
    /*@GetMapping("/search")
    public String getbyOption(String type, Model map, String option, @RequestParam(value = "page", defaultValue = "1") int page) {
        System.out.println(type);
        System.out.println(option);
        Page<MemberDto> paging = service.getByOption(type, option, page-1);
        map.addAttribute("paging", paging);
        map.addAttribute("type", type);
        map.addAttribute("option", option);
        return "employee/list";
    }*/
}







