package com.example.demo.commute;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Commute {
    @Id
    @SequenceGenerator(name="seq_gen", sequenceName="seq_commute", allocationSize=1)//시퀀스 생성. 생성한 시퀀스 이름:seq_commute
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_gen")//자동으로 값 할당
    private int num; //근태관리 번호

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;   //멤버
    private String basicDate;    //기준일
    private String startTime;   //출근시간
    private String endTime;     //퇴근시간
    private String reason;     //수정사유
    private String editStartTime;   //수정 출근시간
    private String editEndTime;     //수정 퇴근시간
    private String editBasicDate;   //수정 기준일
}	
