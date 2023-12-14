package com.example.demo.dataroom;

import com.example.demo.member.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataroomDto {
    private int num;
    private Member member;
    private String wdate;
    private String title;
    private String content;
    private String fname;
    private int cnt;

    private MultipartFile f;

    public static DataroomDto of(Dataroom dataroom) {
        return new DataroomDto(
                dataroom.getNum(),
                dataroom.getMember(),
                dataroom.getWdate(),
                dataroom.getTitle(),
                dataroom.getContent(),
                dataroom.getFname(),
                dataroom.getCnt(),
                null
        );
    }

}