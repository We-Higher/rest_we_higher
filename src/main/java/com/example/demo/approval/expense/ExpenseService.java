package com.example.demo.approval.expense;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseDao dao;
    
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    public ExpenseDto saveExpense(ExpenseDto dto) {
        Expense e = dao.save(new Expense(dto.getExpenseNum(),dto.getMember(),dto.getTitle(),dto.getContent(),dto.getWdate(),dto.getCategory(),dto.getDetail(),dto.getSum(),dto.getNote(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
        return new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(), e.getRstatus(), e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username());
    }

    public ArrayList<ExpenseDto> getAll(){
        ArrayList<Expense> list = (ArrayList<Expense>) dao.findAll(Sort.by(Sort.Direction.DESC, "expenseNum"));
        ArrayList<ExpenseDto> list2 = new ArrayList<>();
        for(Expense e : list){
            list2.add(new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(), e.getRstatus(), e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username()));
        }
        return list2;
    }

    public ExpenseDto getById(int num){
        Expense e = dao.getById(num);
        return new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(), e.getRstatus(), e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username());
    }

    public ArrayList<ExpenseDto> getByMember(Member m){
        ArrayList<Expense> list = dao.findByMember(m);
        ArrayList<ExpenseDto> list2 = new ArrayList<>();
        for(Expense e : list){
            list2.add(new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(), e.getRstatus(), e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username()));
        }
        return list2;
    }
    
    public ExpenseDto approveExpense(HttpServletResponse response, ExpenseDto dto, MemberDto mdto) throws IOException {
    	
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
    	
    	Expense e = dao.save(new Expense(dto.getExpenseNum(),dto.getMember(),dto.getTitle(),dto.getContent(),dto.getWdate(),dto.getCategory(),dto.getDetail(),dto.getSum(),dto.getNote(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
        return new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(),e.getRstatus(),e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username());
    }
    
    public ExpenseDto refuseExpense(HttpServletResponse response, ExpenseDto dto, MemberDto mdto) throws IOException {
    	
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
    	
    	Expense e = dao.save(new Expense(dto.getExpenseNum(),dto.getMember(),dto.getTitle(),dto.getContent(),dto.getWdate(),dto.getCategory(),dto.getDetail(),dto.getSum(),dto.getNote(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
        return new ExpenseDto(e.getExpenseNum(),e.getMember(),e.getTitle(),e.getContent(),e.getWdate(),e.getCategory(),e.getDetail(),e.getSum(),e.getNote(),e.getStatus(),e.getRstatus(),e.getApproval1(),e.getApproval2(), e.getApproval1rank(), e.getApproval2rank(), e.getApp1username(), e.getApp2username());
    }
}
