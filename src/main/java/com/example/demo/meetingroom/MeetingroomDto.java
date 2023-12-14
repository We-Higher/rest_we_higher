package com.example.demo.meetingroom;

import com.example.demo.member.Member;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingroomDto {
    private int id;
    private Member member;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int roomId;

    public MeetingroomDto toDto(Meetingroom m){
        return MeetingroomDto.builder()
                .id(m.getId())
                .member(m.getMember())
                .title(m.getTitle())
                .startDate(m.getStartDate())
                .endDate(m.getEndDate())
                .roomId(m.getRoomId())
                .build();
    }
}
