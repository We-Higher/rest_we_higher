package com.example.demo.approval.vacation;

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
public class Vacation {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_vacation", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    private int vacationNum; //휴가신청서 고유번호

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;   //멤버
    private String type;   //휴가종류
    private String startDate; //휴가 시작일
    private String endDate; //휴가 종료일
    private String reason; //휴가사유
    private String wdate; //작성일
    @Column(columnDefinition = "int default 0", nullable = false)
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
