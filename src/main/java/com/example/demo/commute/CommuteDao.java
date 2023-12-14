package com.example.demo.commute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CommuteDao extends JpaRepository<Commute, Integer> {
	
    ArrayList<Commute> findByMemberIdOrderByNumDesc(Long id);    //회원번호로 검색
    
    Commute findByBasicDateAndMemberUsername(String BasicDate, String Username);
	
    Page<Commute> findByBasicDateLike(String BasicDate, Pageable pageable); //기준일로 검색

    Page<Commute> findByMemberNameContaining(String name, Pageable pageable);  //이름으로 검색

    Page<Commute> findByMemberDeptNameContaining(String deptName, Pageable pageable);  //부서명으로 검색

    Page<Commute> findByMemberId(Long id, Pageable pageable);    //회원번호로 검색
    

    Page<Commute> findByBasicDateLikeAndMemberId(String BasicDate, Long id, Pageable pageable);
}
