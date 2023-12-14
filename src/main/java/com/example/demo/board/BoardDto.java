package com.example.demo.board;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.example.demo.member.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardDto {
	private int num;
	private String wdate;
	private String udate;
	private Member member;
	private String title;
	private String content;
    private int cnt;

	public static BoardDto of(Board board) {
		return new BoardDto(
			board.getNum(),
			board.getWdate(),
			board.getUdate(),
			board.getMember(),
			board.getTitle(),
			board.getContent(),
			board.getCnt()
		);
	}
}
