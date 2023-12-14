package com.example.demo.approval.report;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.approval.expense.Expense;

@Repository
public interface ReportDao extends JpaRepository<Report, Integer> {
	
	ArrayList<Report> findByMemberUsernameOrderByReportNumDesc(String username);
    ArrayList<Report> findByApp1usernameAndStatusAndRstatusOrderByReportNumDesc(String username, int status, int rstatus); 
    ArrayList<Report> findByApp2usernameAndStatusAndRstatusOrderByReportNumDesc(String username, int status, int rstatus);
    ArrayList<Report> findByMemberUsernameAndRstatusOrderByReportNumDesc(String username, int rstatus);
}
