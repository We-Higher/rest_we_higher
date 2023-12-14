package com.example.demo.approval.expense;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Expense {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_expense", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    private int expenseNum; //품의서 고유번호

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;   //멤버
    private String title;   //제목
    private String content; //사유
    private String wdate; //작성일
    private String category; //분류
    private String detail; //내역
    private String sum;     //금액
    private String note;    //비고
    private int status;     //결재 상태
    @Column(columnDefinition = "int default 0", nullable = false)
    private int rstatus;     //반려 상태
    private String approval1;  //1차 결재자
    private String approval2;  //2차 결재자
    private String approval1rank;  //1차 결재자 직급
    private String approval2rank;  //2차 결재자 직급
    private String app1username;  //1차 결재자 username
    private String app2username;  //2차 결재자 username
}
