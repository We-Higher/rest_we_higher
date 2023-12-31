package com.example.demo.approval.expense;

import com.example.demo.approval.report.ReportDto;
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
public class ExpenseController {
    @Autowired
    private ExpenseService eservice;
    @Autowired
    private MemberService mservice;

    //자바에서 script 사용하기
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    //지출결의서
	@GetMapping("/expense")
	public Map Expense() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		Map map = new HashMap();
		map.put("mdto", mdto);
		return map;
	}

	@PostMapping("/expense")
	public Map addExpense(ExpenseDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(),
				mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(),
				mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(),
				mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(),
				mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
		eservice.saveExpense(dto);
		Map map = new HashMap();
		map.put("dto", dto);
		return map;
	}
	
	@RequestMapping("/expense/editread/{num}")
	public Map get(@PathVariable("num") int num) {

		Map map = new HashMap();
		ExpenseDto edto = eservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", edto);
		return map;
	}
	
	@RequestMapping("/expense/edit/{num}")
	public Map get2(@PathVariable("num") int num) {

		Map map = new HashMap();
		ExpenseDto edto = eservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", edto);
		return map;
	}
	
    @PostMapping("/expense/approve")
    public Map ExpenseApproval(int num) {

		Map map = new HashMap();
		boolean flag = true;
		ExpenseDto edto = eservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(edto.getRstatus()==0 && edto.getStatus()==0 && edto.getApp1username().equals(mdto.getUsername())){
    		eservice.approveExpense(edto, mdto);
    	}
    	else if(edto.getRstatus()==0 && edto.getStatus()==1 && edto.getApp2username().equals(mdto.getUsername())){
    		eservice.approveExpense(edto, mdto);
    	}
    	else {
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
    
    @PostMapping("/expense/refuse")
    public Map ExpenseRefuse(int num){
    	
		Map map = new HashMap();
		boolean flag = true;
		ExpenseDto edto = eservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(edto.getRstatus()==0 && edto.getStatus()==0 && edto.getApp1username().equals(mdto.getUsername())){
    		eservice.refuseExpense(edto, mdto);
    	}
    	else if(edto.getRstatus()==0 && edto.getStatus()==1 && edto.getApp2username().equals(mdto.getUsername())){
    		eservice.refuseExpense(edto, mdto);
    	}
    	else {
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
}
