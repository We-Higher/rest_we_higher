package com.example.demo.approval.vacation;

import com.example.demo.approval.vacation.VacationDto;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/approval")
public class VacationController {
	
    @Autowired
    private VacationService vservice;
    @Autowired
    private MemberService mservice;
    
	@GetMapping("/vacation")
	public Map Vacation() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		Map map = new HashMap();
		map.put("mdto", mdto);
		return map;
	}
	
	@PostMapping("/vacation")
	public Map addVacation(VacationDto dto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(),
				mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(),
				mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(),
				mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(),
				mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(), mdto.getMonthMember()));
		vservice.saveVacation(dto);
		Map map = new HashMap();
		map.put("dto", dto);
		return map;
	}
	
	@RequestMapping("/vacation/editread/{num}")
	public Map get(@PathVariable("num") int num) {

		Map map = new HashMap();
		VacationDto vdto = vservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", vdto);
		return map;
	}
	
	@RequestMapping("/vacation/edit/{num}")
	public Map get2(@PathVariable("num") int num) {

		Map map = new HashMap();
		VacationDto vdto = vservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
		map.put("mdto", mdto);
		map.put("dto", vdto);
		return map;
	}
	
    @PostMapping("/vacation/approve")
    public Map VacationApproval(int num) {

		Map map = new HashMap();
		boolean flag = true;
		VacationDto vdto = vservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(vdto.getRstatus()==0 && vdto.getStatus()==0 && vdto.getApp1username().equals(mdto.getUsername())){
    		vservice.approveVacation(vdto, mdto);
    	}
    	else if(vdto.getRstatus()==0 && vdto.getStatus()==1 && vdto.getApp2username().equals(mdto.getUsername())){
    		vservice.approveVacation(vdto, mdto);
    	}
    	else {
    		
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
    
    @PostMapping("/vacation/refuse")
    public Map VacationRefuse(int num){
    	
		Map map = new HashMap();
		boolean flag = true;
		VacationDto vdto = vservice.getById(num);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		MemberDto mdto = mservice.getMember(id);
    	if(vdto.getRstatus()==0 && vdto.getStatus()==0 && vdto.getApp1username().equals(mdto.getUsername())){
    		vservice.refuseVacation(vdto, mdto);
    	}
    	else if(vdto.getRstatus()==0 && vdto.getStatus()==1 && vdto.getApp2username().equals(mdto.getUsername())){
    		vservice.refuseVacation(vdto, mdto);
    	}
    	else {
    		
    		flag = false;
    		System.out.println("결재할 수 없습니다.");
    	}
    	map.put("flag", flag);
        return map;
    }
}
