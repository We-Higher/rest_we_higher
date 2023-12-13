package com.example.demo.approval.report;

import com.example.demo.member.MemberDto;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@Service
public class ReportService {
    @Autowired
    private ReportDao dao;
    
    //자바에서 script 사용하기
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    public ReportDto saveReport(ReportDto dto) {
    	
    	Report r = dao.save(new Report(dto.getReportNum(), dto.getMember(), dto.getTitle(), dto.getContent(), dto.getWdate(), dto.getServiceLife(), dto.getClassification(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
    	return new ReportDto(r.getReportNum(), r.getMember(), r.getTitle(), r.getContent(), r.getWdate(), r.getServiceLife(), r.getClassification(), r.getStatus(), r.getRstatus(), r.getApproval1(),r.getApproval2(), r.getApproval1rank(), r.getApproval2rank(), r.getApp1username(), r.getApp2username());
    }

    public ArrayList<ReportDto> getAll() {
        ArrayList<Report> list = (ArrayList<Report>) dao.findAll(Sort.by(Sort.Direction.DESC, "reportNum"));
        ArrayList<ReportDto> list2 = new ArrayList<>();
        for (Report r : list) {
            list2.add(new ReportDto(r.getReportNum(), r.getMember(), r.getTitle(), r.getContent(), r.getWdate(), r.getServiceLife(), r.getClassification(), r.getStatus(), r.getRstatus(), r.getApproval1(),r.getApproval2(), r.getApproval1rank(), r.getApproval2rank(), r.getApp1username(), r.getApp2username()));
        }
        return list2;
    }

    public ReportDto getById(int num) {
        Report r = dao.getById(num);
        return new ReportDto(r.getReportNum(), r.getMember(), r.getTitle(), r.getContent(), r.getWdate(), r.getServiceLife(), r.getClassification(), r.getStatus(), r.getRstatus(), r.getApproval1(),r.getApproval2(), r.getApproval1rank(), r.getApproval2rank(), r.getApp1username(), r.getApp2username());
    }
    
    public ReportDto approveReport(HttpServletResponse response, ReportDto dto, MemberDto mdto) throws IOException {
    	
    	if(dto.getRstatus()==0 && dto.getStatus()==0 && dto.getApp1username().equals(mdto.getUsername())){
    		dto.setStatus(1);
    	}
    	else if(dto.getRstatus()==0 && dto.getStatus()==1 && dto.getApp2username().equals(mdto.getUsername())){
    		dto.setStatus(2);
    	}
    	else {
    		
            PrintWriter out = response.getWriter();
            out.write("<script>alert('"+"결재할 수 없습니다."+"');location.href='"+"/approval/process"+"';</script>");
            out.flush();
    	}
    	
    	Report r = dao.save(new Report(dto.getReportNum(), dto.getMember(), dto.getTitle(), dto.getContent(), dto.getWdate(), dto.getServiceLife(), dto.getClassification(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
    	return new ReportDto(r.getReportNum(), r.getMember(), r.getTitle(), r.getContent(), r.getWdate(), r.getServiceLife(), r.getClassification(), r.getStatus(), r.getRstatus(), r.getApproval1(),r.getApproval2(), r.getApproval1rank(), r.getApproval2rank(), r.getApp1username(), r.getApp2username());
    }
    
    public ReportDto refuseReport(HttpServletResponse response, ReportDto dto, MemberDto mdto) throws IOException {
    	
    	if(dto.getRstatus()==0 && dto.getStatus()==0 && dto.getApp1username().equals(mdto.getUsername())){
    		dto.setRstatus(-1);
    	}
    	else if(dto.getRstatus()==0 && dto.getStatus()==1 && dto.getApp2username().equals(mdto.getUsername())){
    		dto.setRstatus(-1);
    	}
    	else {
    		
            PrintWriter out = response.getWriter();
            out.write("<script>alert('"+"반려할 수 없습니다."+"');location.href='"+"/approval/process"+"';</script>");
            out.flush();
    	}
    	
    	Report r = dao.save(new Report(dto.getReportNum(), dto.getMember(), dto.getTitle(), dto.getContent(), dto.getWdate(), dto.getServiceLife(), dto.getClassification(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
    	return new ReportDto(r.getReportNum(), r.getMember(), r.getTitle(), r.getContent(), r.getWdate(), r.getServiceLife(), r.getClassification(), r.getStatus(), r.getRstatus(), r.getApproval1(),r.getApproval2(), r.getApproval1rank(), r.getApproval2rank(), r.getApp1username(), r.getApp2username());
    }
}
