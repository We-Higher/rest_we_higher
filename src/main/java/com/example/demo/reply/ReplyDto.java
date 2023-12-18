package com.example.demo.reply;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.auth.SecurityMember;
import com.example.demo.board.Board;
import com.example.demo.commute.Commute;
import com.example.demo.commute.CommuteDto;
import com.example.demo.member.Member;
import com.example.demo.schedule.ScheduleDto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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

	public static ReplyDto of(Reply Reply) {
		return new ReplyDto(
			Reply.getNum(),
			Reply.getWdate(),
			Reply.getUdate(),
			Reply.getBoard(),
			Reply.getTitle(),
			Reply.getContent(),
			Reply.getMember()
		);
	}

}
