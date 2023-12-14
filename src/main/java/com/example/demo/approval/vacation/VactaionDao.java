package com.example.demo.approval.vacation;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.approval.report.Report;
import com.example.demo.approval.vacation.Vacation;

@Repository
public interface VactaionDao extends JpaRepository<Vacation, Integer> {
	
	ArrayList<Vacation> findByMemberUsernameOrderByVacationNumDesc(String username);
    ArrayList<Vacation> findByApp1usernameAndStatusAndRstatusOrderByVacationNumDesc(String username, int status, int rstatus); 
    ArrayList<Vacation> findByApp2usernameAndStatusAndRstatusOrderByVacationNumDesc(String username, int status, int rstatus);
    ArrayList<Vacation> findByMemberUsernameAndRstatusOrderByVacationNumDesc(String username, int rstatus);
}
