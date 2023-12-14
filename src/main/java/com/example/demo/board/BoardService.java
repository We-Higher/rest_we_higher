package com.example.demo.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;

@Service
public class BoardService {
	@Autowired
	private BoardDao dao;
	
    // pk로 검색. dao.findById()
    public BoardDto getBoard(int num) {
        Board b = dao.findById(num).orElse(null);
        return new BoardDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt());
    }
    
    // pk로 검색. dao.findById()
    public Board getBoard2(int num) {
        Board b = dao.findById(num).orElse(null);
        return b;
    }
    
    // 전체검색. dao.findAll()
    public ArrayList<BoardDto> getAll() {
        List<Board> list = dao.findAll(Sort.by(Sort.Direction.DESC, "num"));
        ArrayList<BoardDto> list2 = new ArrayList<>();
        for (Board b : list) {
            list2.add(new BoardDto(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
        }
        return list2;
    }
    
    // 옵션으로 검색
    public Page<BoardDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "num"));
        Page<Board> list;
        if (Objects.equals("name", type)) {
            list = dao.findByMemberNameLike("%" + option + "%", pageable);
        } else if (Objects.equals("title", type)) {
            list = dao.findByTitleLike("%" + option + "%", pageable);
        } else {
            list = dao.findAll(pageable);
        }
        return list.map(BoardDto::of);
    }
    
    // 게시글 추가, 수정. dao.save()
    public BoardDto saveBoard(BoardDto b, int check) {

        if (check == 0) { //추가

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setUdate(formattedTime1);
            b.setWdate(formattedTime1);
            Board b2 = dao.save(new Board(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new BoardDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b2.getCnt());
        } else if (check == 1) {  //수정

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedTime1 = LocalDateTime.now().format(formatter1);
            b.setUdate(formattedTime1);
            Board b2 = dao.save(new Board(b.getNum(), b.getWdate(), b.getUdate(), b.getMember(), b.getTitle(), b.getContent(), b.getCnt()));
            return new BoardDto(b2.getNum(), b2.getWdate(), b2.getUdate(), b2.getMember(), b2.getTitle(), b2.getContent(), b2.getCnt());
        } else return null;
    }
    
    // 게시글 pk 기준 삭제. dao.deleteById()
    public void delBoard(int num) {
        dao.deleteById(num);
    }
    
    // 조회수 증가
    public void editCnt(int num) {
    	dao.updateCnt(num);
    }
    
    public Page<Board> getBoardList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "num"));
        return this.dao.findAll(pageable);
    }
}









