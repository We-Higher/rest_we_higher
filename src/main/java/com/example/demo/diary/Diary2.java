package com.example.demo.diary;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.member.Member;

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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Diary2 {
	@Id // pk지정
	@SequenceGenerator(name = "seq_gen", sequenceName = "seq_diary1", allocationSize = 1) 																	// 이름:seq_board2
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
	private int num;
	private Date wdate;
	@ManyToOne
	@JoinColumn(nullable=false)//member2(id)에 조인. 널 허용 안함
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member writer;
	private String title;
	private String content;
	private String fname;

	@PrePersist // insert문 실행전 자동 호출
	public void setDate() {
		wdate = new Date();
	}
}
