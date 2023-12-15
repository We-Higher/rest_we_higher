package com.example.demo.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotifyDao extends JpaRepository<Notify, Integer> {
	
    @Transactional
    @Modifying
    @Query(value="update notify set cnt=cnt+1 where num=:num", nativeQuery=true)
    void updateCnt(@Param("num") int num);
	Page<Notify> findByMemberNameLike(String name, Pageable pageable);  //이름으로 검색
    Page<Notify> findByTitleLike(String name, Pageable pageable);  //제목으로 검색
}

