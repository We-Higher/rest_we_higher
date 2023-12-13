package com.example.demo.approval.report;

import com.example.demo.member.Member;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportDto {
    private int reportNum; //품의서 고유번호
    private Member member;   //멤버
    private String title;   //제목
    private String content; //상세내용
    private String wdate; //기안일
    private String serviceLife; //보존연한
    private String classification; //비밀등급
    private int status;     //결재 상태
    private int rstatus;     //반려 상태
    private String approval1;  //1차 결재자
    private String approval2;  //2차 결재자
    private String approval1rank;  //1차 결재자 직급
    private String approval2rank;  //2차 결재자 직급
    private String app1username;  //1차 결재자 username
    private String app2username;  //2차 결재자 username
}
