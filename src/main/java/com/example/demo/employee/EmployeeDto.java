package com.example.demo.employee;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeDto {

	private Long id;  //employee 시퀀스
    private String name; // 이름
    private String username; // 아이디
    private String newNo; // 사번
    private int deptCode; // 부서 코드
    private String deptName; // 부서명
    private int companyRank; // 직급
    private String companyRankName; // 직급명
    private String phone; // 휴대전화
    private String email; // 이메일
    private String comCall; // 내선 전화
}
