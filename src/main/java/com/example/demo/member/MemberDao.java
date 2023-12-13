package com.example.demo.member;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberDao extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    //Member findByUsername(String username);
    Member findByName(String name);
    /*Page<Member> findByNameLike(String name, Pageable pageable);  //이름으로 검색
    Page<Member> findByNewNoLike(String newNo, Pageable pageable);  //사번으로 검색
    Page<Member> findByCompanyRankNameLike(String companyRankName, Pageable pageable);  //직급으로 검색
    Page<Member> findByDeptNameLike(String deptName, Pageable pageable);  //부서명으로 검색*/
    
    ArrayList<Member> findByNameLike(String name);  //이름으로 검색
    ArrayList<Member> findByNewNoLike(String newNo);  //사번으로 검색
    ArrayList<Member> findByCompanyRankNameLike(String companyRankName);  //직급으로 검색
    ArrayList<Member> findByDeptNameLike(String deptName);  //부서명으로 검색

    // 특정 사용자 제외 조회(채팅 초대)
    ArrayList<Member> findByIdNot(Long id, Sort sort);
	
}
