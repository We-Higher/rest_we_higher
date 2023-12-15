package com.example.demo.approval.report;

import com.example.demo.board.BoardDto;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/approval")
public class ReportController {

	@Autowired
	private ReportService rservice;
	@Autowired
	private MemberService mservice;

	// 자바에서 script 사용하기
	public static void init(HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
	}

	// 품의서
	@GetMapping("/report")
	public Map report() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		Map map = new HashMap();
		map.put("mdto", mdto);
		return map;
	}
	
	//품의서 기안
	@PostMapping("/report")
	public Map addReport(ReportDto dto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(),
				mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(),
				mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(),
				mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(),
				mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
		rservice.saveReport(dto);
		Map map = new HashMap();
		map.put("dto", dto);
		return map;
	}
	
	@RequestMapping("/report/editread/{num}")
	public Map get(@PathVariable("num") int num) {

		Map map = new HashMap();
		ReportDto rdto = rservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", rdto);
		return map;
	}
	
	@RequestMapping("/report/edit/{num}")
	public Map get2(@PathVariable("num") int num) {

		Map map = new HashMap();
		ReportDto rdto = rservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", rdto);
		return map;
	}
	
    @PostMapping("/report/approve")
    public Map ReportApproval(int num) {

		Map map = new HashMap();
		boolean flag = true;
		ReportDto rdto = rservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(rdto.getRstatus()==0 && rdto.getStatus()==0 && rdto.getApp1username().equals(mdto.getUsername())){
    		rservice.approveReport(rdto, mdto);
    	}
    	else if(rdto.getRstatus()==0 && rdto.getStatus()==1 && rdto.getApp2username().equals(mdto.getUsername())){
    		rservice.approveReport(rdto, mdto);
    	}
    	else {
    		
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
    
    @PostMapping("/report/refuse")
    public Map ReportRefuse(int num){
    	
		Map map = new HashMap();
		boolean flag = true;
		ReportDto rdto = rservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(rdto.getRstatus()==0 && rdto.getStatus()==0 && rdto.getApp1username().equals(mdto.getUsername())){
    		rservice.refuseReport(rdto, mdto);
    	}
    	else if(rdto.getRstatus()==0 && rdto.getStatus()==1 && rdto.getApp2username().equals(mdto.getUsername())){
    		rservice.refuseReport(rdto, mdto);
    	}
    	else {
    		
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
}
