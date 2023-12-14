package com.example.demo.meetingroom;

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
@Builder
public class Meetingroom {
    @Id
    @SequenceGenerator(name="mtr_gen", sequenceName="seq_meetingroom", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="mtr_gen")
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
    private int roomId;

    public Meetingroom toEntity(MeetingroomDto dto){
        return Meetingroom.builder()
                .id(dto.getId())
                .member(dto.getMember())
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .roomId(dto.getRoomId())
                .build();
    }

}
