package com.example.demo.employee;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.example.demo.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity  //jpa table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {
	
	@Id //pk지정
	@SequenceGenerator(name="seq_gen", sequenceName="seq_emp1", allocationSize=1)//시퀀스 생성. 생성한 시퀀스 이름:seq_emp1
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen")//자동으로 값 할당
	private int num;
    private String name; // 이름
    private String username; // 아이디
    private int newNo; // 사번
    private int deptCode; // 부서 코드
    private String deptName; // 부서명
    private int companyRank; // 직급
    private String companyRankName; // 직급명
    private String phone; // 휴대전화
    private String email; // 이메일
    private String comCall; // 내선 전화
	
	/*@ManyToOne
	@JoinColumn(nullable=false)//member(id)에 조인. 널 허용 안함
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member writer;
	private String title;
	private String content;
	
	@PrePersist //insert문 실행전 자동 호출
	public void setDate() {
		wdate = new Date();
	}*/
}
