package com.example.demo.approval.vacation;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
@PreAuthorize("isAuthenticated()")
@Controller
public class VacationController {
    @Autowired
    private VacationService vservice;
    @Autowired
    private MemberService mservice;

    //자바에서 script 사용하기
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    //휴가신청서
    @GetMapping("/vacation")
    public ModelAndView vacation(Principal principal) {
        ModelAndView mav = new ModelAndView("approval/vacation");
        MemberDto mdto = mservice.getMember(principal.getName());
        mav.addObject("m", mdto);
        return mav;
    }

    @PostMapping("/vacation")
    public void addVacation(HttpServletResponse response, VacationDto dto, Principal principal) {
        try {
            init(response);
            PrintWriter out = response.getWriter();
            MemberDto mdto = mservice.getMember(principal.getName());
            dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(),mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(),mdto.getMonthMember()));
            vservice.saveVacation(dto);
            out.println(String.format("<script>window.close();</script>"));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
