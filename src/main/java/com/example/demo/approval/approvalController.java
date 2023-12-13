package com.example.demo.approval;

import com.example.demo.approval.expense.ExpenseDto;
import com.example.demo.approval.expense.ExpenseService;
import com.example.demo.approval.report.ReportDto;
import com.example.demo.approval.report.ReportService;
import com.example.demo.approval.vacation.VacationDto;
import com.example.demo.approval.vacation.VacationService;
import com.example.demo.employee.EmployeeDto;
import com.example.demo.employee.EmployeeService;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;

@PreAuthorize("isAuthenticated()")
@Controller
@RequestMapping("/approval")
public class approvalController {
	
    @Autowired
    private ExpenseService eservice;
    @Autowired
    private ReportService rservice;
    @Autowired
    private VacationService vservice;
    @Autowired
    private EmployeeService empservice;
    @Autowired
    private MemberService mservice;

    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    @GetMapping("/draft")
    public void list(Model map) {
        ArrayList<ExpenseDto> elist = eservice.getAll();
        ArrayList<ReportDto> rlist = rservice.getAll();
        ArrayList<VacationDto> vlist = vservice.getAll();
        map.addAttribute("elist", elist);
        map.addAttribute("rlist", rlist);
        map.addAttribute("vlist", vlist);
    }

    @GetMapping("/mydraft")
    public void listByMember(Model map) {
        ArrayList<ExpenseDto> elist = eservice.getAll();
        ArrayList<ReportDto> rlist = rservice.getAll();
        ArrayList<VacationDto> vlist = vservice.getAll();
        map.addAttribute("elist", elist);
        map.addAttribute("rlist", rlist);
        map.addAttribute("vlist", vlist);
    }

    @GetMapping("/process")
    public void listById(Model map, Principal principal) {
        ArrayList<ExpenseDto> elist = eservice.getAll();
        ArrayList<ReportDto> rlist = rservice.getAll();
        ArrayList<VacationDto> vlist = vservice.getAll();
        MemberDto mdto = mservice.getMember(principal.getName());
        map.addAttribute("m", mdto);
        map.addAttribute("elist", elist);
        map.addAttribute("rlist", rlist);
        map.addAttribute("vlist", vlist);
    }

    @GetMapping("/myrefuse")
    public void RefuseListById(Model map, Principal principal) {
        ArrayList<ExpenseDto> elist = eservice.getAll();
        ArrayList<ReportDto> rlist = rservice.getAll();
        ArrayList<VacationDto> vlist = vservice.getAll();
        MemberDto mdto = mservice.getMember(principal.getName());
        map.addAttribute("m", mdto);
        map.addAttribute("elist", elist);
        map.addAttribute("rlist", rlist);
        map.addAttribute("vlist", vlist);
    }

    @GetMapping("/expense/edit")
    public String editExpense(ExpenseDto edto, Model map) {
        edto = eservice.getById(edto.getExpenseNum());
        map.addAttribute("edto", edto);
        return "approval/edit/editExpense";
    }

    @GetMapping("/report/edit")
    public String editReport(ReportDto rdto, Model map) {
        rdto = rservice.getById(rdto.getReportNum());
        System.out.println("rdto = " + rdto);
        map.addAttribute("rdto", rdto);
        return "approval/edit/editReport";
    }

    @GetMapping("/vacation/edit")
    public String editVacation(VacationDto vdto, Model map) {
        vdto = vservice.getById(vdto.getVacationNum());
        System.out.println("vdto = " + vdto);
        map.addAttribute("vdto", vdto);
        return "approval/edit/editVacation";
    }

    @GetMapping("/expense/editread")
    public String editExpenseRead(ExpenseDto edto, Model map) {
        edto = eservice.getById(edto.getExpenseNum());
        map.addAttribute("edto", edto);
        return "approval/edit/editExpenseRead";
    }

    @GetMapping("/report/editread")
    public String editReportRead(ReportDto rdto, Model map) {
        rdto = rservice.getById(rdto.getReportNum());
        System.out.println("rdto = " + rdto);
        map.addAttribute("rdto", rdto);
        return "approval/edit/editReportRead";
    }

    @GetMapping("/vacation/editread")
    public String editVacationRead(VacationDto vdto, Model map) {
        vdto = vservice.getById(vdto.getVacationNum());
        System.out.println("vdto = " + vdto);
        map.addAttribute("vdto", vdto);
        return "approval/edit/editVacationRead";
    }

    //1차 결재자 선택
    @GetMapping("/approvalList1")
    public ModelAndView approvalList1() {
        ModelAndView mav = new ModelAndView("approval/approvalList1");
        ArrayList<EmployeeDto> list = empservice.getAll();
        mav.addObject("list", list);
        return mav;
    }

    //1차 결재자 검색
    /*@PostMapping("/approvalList1")
    public ModelAndView getbyOption(String type, String option, Pageable pageable, @RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView("approval/approvalList1");
        Page<MemberDto> list = empservice.getByOption(type, option, page);
        mav.addObject("list", list);
        return mav;
    }

    //2차 결재자 선택
    @GetMapping("/approvalList2")
    public ModelAndView approvalList2() {
        ModelAndView mav = new ModelAndView("approval/approvalList2");
        ArrayList<EmployeeDto> list = empservice.getAll();
        mav.addObject("list", list);
        return mav;
    }

    //2차 결재자 검색
    @PostMapping("/approvalList2")
    public ModelAndView getbyOption2(String type, String option, Pageable pageable, @RequestParam(value = "page", defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView("approval/approvalList2");
        Page<MemberDto> list = empservice.getByOption(type, option, page);
        mav.addObject("list", list);
        return mav;
    }*/

    //품의서 결재
    @PostMapping("/report/approve")
    public String ReportApproval(HttpServletResponse response, int reportNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        ReportDto rdto = rservice.getById(reportNum);
        rservice.approveReport(response, rdto, mdto);

        return "redirect:/approval/process";
    }

    //지출결의서 결재
    @PostMapping("/expense/approve")
    public String ExpenseApproval(HttpServletResponse response, int expenseNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        ExpenseDto edto = eservice.getById(expenseNum);
        eservice.approveExpense(response, edto, mdto);

        return "redirect:/approval/process";
    }

    //휴가신청서 결재
    @PostMapping("/vacation/approve")
    public String VacationApproval(HttpServletResponse response, int vacationNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        VacationDto vdto = vservice.getById(vacationNum);
        vservice.approveVacation(response, vdto, mdto);

        return "redirect:/approval/process";
    }

    //품의서 반려
    @GetMapping("/report/refuse")
    public String ReportRefuse(HttpServletResponse response, int reportNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        ReportDto rdto = rservice.getById(reportNum);
        rservice.refuseReport(response, rdto, mdto);

        return "redirect:/approval/process";
    }

    //지출결의서 반려
    @GetMapping("/expense/refuse")
    public String ExpenseRefuse(HttpServletResponse response, int expenseNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        ExpenseDto edto = eservice.getById(expenseNum);
        eservice.refuseExpense(response, edto, mdto);

        return "redirect:/approval/process";
    }

    //휴가신청서 반려
    @GetMapping("/vacation/refuse")
    public String VacationRefuse(HttpServletResponse response, int vacationNum, Principal principal) throws IOException {

        init(response);
        MemberDto mdto = mservice.getMember(principal.getName());
        VacationDto vdto = vservice.getById(vacationNum);
        vservice.refuseVacation(response, vdto, mdto);

        return "redirect:/approval/process";
    }
}
