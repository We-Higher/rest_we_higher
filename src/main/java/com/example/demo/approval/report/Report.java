package com.example.demo.approval.report;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Report {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_report", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    private int reportNum; //품의서 고유번호

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;   //멤버
    private String title;   //제목
    private String content; //상세내용
    private String wdate; //기안일
    private String serviceLife; //보존연한
    private String classification; //비밀등급
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
