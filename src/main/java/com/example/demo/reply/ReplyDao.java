package com.example.demo.reply;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.Board;
import com.example.demo.member.Member;

@Repository
public interface ReplyDao extends JpaRepository<Reply, Integer> {

    ArrayList<Reply> findByBoardNumOrderByNum(int num);

}

