package com.example.demo.dataroom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DataroomDao extends JpaRepository<Dataroom,Integer> {
    @Transactional
    @Modifying
    @Query(value="update dataroom set cnt=cnt+1 where num=:num", nativeQuery=true)
    void updateCnt(@Param("num") int num);
    Page<Dataroom> findByMemberNameContains(String writer, Pageable pageable);
    Page<Dataroom> findByTitleContains(String title, Pageable pageable);
    Page<Dataroom> findAll(Pageable pageable); //페이징
}