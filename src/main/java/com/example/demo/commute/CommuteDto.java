package com.example.demo.commute;

import com.example.demo.dataroom.Dataroom;
import com.example.demo.dataroom.DataroomDto;
import com.example.demo.member.Member;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommuteDto {
    private int num; //근태관리 번호
    private Member member;   //멤버
    private String basicDate;    //기준일
    private String startTime;   //출근시간
    private String endTime;     //퇴근시간
    private String reason;     //수정사유
    private String editStartTime;   //수정 출근시간
    private String editEndTime;     //수정 퇴근시간
    private String editBasicDate;   //수정 기준일

    public static CommuteDto of(Commute commute) {
        return new CommuteDto(
                commute.getNum(),
                commute.getMember(),
                commute.getBasicDate(),
                commute.getStartTime(),
                commute.getEndTime(),
                commute.getReason(),
                commute.getEditStartTime(),
                commute.getEditEndTime(),
                commute.getEditBasicDate()
        );
    }
}
