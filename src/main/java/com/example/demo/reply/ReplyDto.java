package com.example.demo.reply;

import com.example.demo.member.Member;
import com.example.demo.board.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReplyDto {

	private int num;
	private String wdate;
	private String udate;
	private Board board;
	private String title;
	private String content;
	private Member member;

	public static ReplyDto of(Reply reply) {
		return new ReplyDto(
			reply.getNum(),
			reply.getWdate(),
			reply.getUdate(),
			reply.getBoard(),
			reply.getTitle(),
			reply.getContent(),
			reply.getMember()
		);
	}

}
