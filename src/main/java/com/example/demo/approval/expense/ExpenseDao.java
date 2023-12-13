package com.example.demo.approval.expense;

import com.example.demo.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ExpenseDao extends JpaRepository<Expense, Integer> {
    ArrayList<Expense> findByMember(Member m);//이름으로 검색
}
