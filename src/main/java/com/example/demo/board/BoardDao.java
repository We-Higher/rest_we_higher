package com.example.demo.board;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.Member;

@Repository
public interface BoardDao extends JpaRepository<Board, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update board set cnt=cnt+1 where num=:num", nativeQuery = true)
    void updateCnt(@Param("num") int num);

    Page<Board> findByMemberNameLike(String name, Pageable pageable);  //이름으로 검색

    Page<Board> findByTitleLike(String title, Pageable pageable);  //제목으로 검색
}

