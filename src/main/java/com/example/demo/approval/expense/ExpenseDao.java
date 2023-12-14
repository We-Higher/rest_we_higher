package com.example.demo.approval.expense;

import com.example.demo.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ExpenseDao extends JpaRepository<Expense, Integer> {
	
    ArrayList<Expense> findByMember(Member m);//이름으로 검색
    ArrayList<Expense> findByMemberUsernameOrderByExpenseNumDesc(String username);//이름으로 검색
    ArrayList<Expense> findByApp1usernameAndStatusAndRstatusOrderByExpenseNumDesc(String username, int status, int rstatus); 
    ArrayList<Expense> findByApp2usernameAndStatusAndRstatusOrderByExpenseNumDesc(String username, int status, int rstatus);
    ArrayList<Expense> findByMemberUsernameAndRstatusOrderByExpenseNumDesc(String username, int rstatus);
}
