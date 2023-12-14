package com.example.demo.approval.expense;

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

@PreAuthorize("isAuthenticated()")
@Controller
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
    public ModelAndView expense(Principal principal) {
        ModelAndView mav = new ModelAndView("approval/expense");
        MemberDto mdto = mservice.getMember(principal.getName());
        mav.addObject("m", mdto);
        return mav;
    }

    @PostMapping("/expense")
    public void addExpense(HttpServletResponse response, ExpenseDto dto, Principal principal) {
        try {
            init(response);
            PrintWriter out = response.getWriter();
            MemberDto mdto = mservice.getMember(principal.getName());
            String[] expenses = dto.getSum().split(",");
            int sum = 0;
            for (String expense : expenses) {
                sum += Integer.parseInt(expense);
            }
            dto.setSum(String.valueOf(sum));
            dto.setMember(new Member(mdto.getId(), mdto.getUsername(), mdto.getPwd(), mdto.getName(), mdto.getEmail(), mdto.getPhone(), mdto.getAddress(), mdto.getCompanyName(), mdto.getDeptCode(), mdto.getDeptName(), mdto.getCompanyRank(), mdto.getCompanyRankName(), mdto.getNewNo(), mdto.getComCall(), mdto.getIsMaster(), mdto.getStatus(), mdto.getCstatus(), mdto.getOriginFname(), mdto.getThumbnailFname(), mdto.getNewMemNo(), mdto.getRemain(),mdto.getMonthMember()));
            eservice.saveExpense(dto);
            out.println(String.format("<script>window.close();</script>"));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
