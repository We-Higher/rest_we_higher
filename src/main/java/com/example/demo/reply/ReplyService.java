package com.example.demo.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.board.Board;
import com.example.demo.board.BoardDao;
import com.example.demo.board.BoardDto;
import com.example.demo.reply.Reply;
import com.example.demo.reply.ReplyDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReplyService {

	@Autowired
	private ReplyDao dao;

	@Autowired
	private BoardDao bdao;

	public ReplyDto getReply(int num) {
		Reply c = dao.findById(num).orElse(null);
		return new ReplyDto(c.getNum(), c.getWdate(), c.getUdate(), c.getBoard(), c.getTitle(), c.getContent(),
				c.getMember());
	}

	public ArrayList<Reply> getList(int num) {
		ArrayList<Reply> r = dao.findByBoardNumOrderByNum(num);
		return r;
	}

	public int getTotal(int num) {
		ArrayList<Reply> r = dao.findByBoardNumOrderByNum(num);
		int cnt = r.size();
		return cnt;
	}

	public ReplyDto saveReply(ReplyDto c) {

		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedTime1 = LocalDateTime.now().format(formatter1);
		c.setUdate(formattedTime1);
		c.setWdate(formattedTime1);
		Reply c2 = dao.save(new Reply(c.getNum(), c.getWdate(), c.getUdate(), c.getBoard(), c.getTitle(),
				c.getContent(), c.getMember()));
		return new ReplyDto(c2.getNum(), c2.getWdate(), c2.getUdate(), c2.getBoard(), c2.getTitle(), c2.getContent(),
				c2.getMember());

	}

	public void delCommute(int num) {
		dao.deleteById(num);
	}

}
