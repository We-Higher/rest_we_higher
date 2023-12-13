package com.example.demo.schedule;

import com.example.demo.member.Member;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {
    private int id;
    private Member member;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int cnt;
}
