package com.example.demo.board;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity  //jpa table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notify {
	@Id //pk지정
	@SequenceGenerator(name="seq_gen", sequenceName="seq_notify1", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen")//자동으로 값 할당
	private int num;
	private String wdate;
	private String udate;
	
	@ManyToOne
	@JoinColumn(nullable=false)//member(id)에 조인. 널 허용 안함
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
	private String title;
	private String content;
    @Column(columnDefinition = "int default 0", nullable = false)
    private int cnt;
}
