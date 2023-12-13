package com.example.demo.schedule;

import com.example.demo.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @SequenceGenerator(name="sch_gen", sequenceName="seq_schedule1", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="sch_gen")
    private int id;
    @ManyToOne
    @JoinColumn(nullable=false)//member(id)에 조인. 널 허용 안함
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(columnDefinition = "int default 0", nullable = false)
    private int cnt;
}