package com.example.demo.employee;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDao;
import com.example.demo.member.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {
    @Autowired
    private MemberDao mdao;

    // 임직원 전체검색
    public ArrayList<EmployeeDto> getAll() {
        List<Member> list = mdao.findAll(Sort.by(Sort.Direction.DESC, "companyRank"));
        ArrayList<EmployeeDto> list2 = new ArrayList<>();
        for (Member b : list) {
            list2.add(new EmployeeDto(b.getId(), b.getName(), b.getUsername(), b.getNewNo(), b.getDeptCode(), b.getDeptName(), b.getCompanyRank(), b.getCompanyRankName(),
                    b.getPhone(), b.getEmail(), b.getComCall()));
        }
        return list2;
    }
    
    // 옵션으로 검색
//    public List<Member> getByOption2(String type, String option) {
//
//        List<Member> list = new ArrayList<Member>();
//        if (Objects.equals("name", type)) {
//            list = mdao.findByNameLike("%" + option + "%");
//        } else if (Objects.equals("newNo", type)) {
//            list = mdao.findByNewNoLike("%" + option + "%");
//        } else if (Objects.equals("companyRankName", type)) {
//            list = mdao.findByCompanyRankNameLike("%" + option + "%");
//        } else if (Objects.equals("deptName", type)) {
//            list = mdao.findByDeptNameLike("%" + option + "%");
//        } else {
//            list = mdao.findAll();
//        }
//        return list;
//    }

    // 페이징 옵션으로 검색
    public Page<MemberDto> getByOption(String type, String option, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "companyRank"));
        Page<Member> list;
        if (Objects.equals("name", type)) {
            list = mdao.findByNameLike("%" + option + "%", pageable);
        } else if (Objects.equals("newNo", type)) {
            list = mdao.findByNewNoLike("%" + option + "%", pageable);
        } else if (Objects.equals("companyRankName", type)) {
            list = mdao.findByCompanyRankNameLike("%" + option + "%", pageable);
        } else if (Objects.equals("deptName", type)) {
            list = mdao.findByDeptNameLike("%" + option + "%", pageable);
        } else {
            list = mdao.findAll(pageable);
        }
        return list.map(MemberDto::of);
    }

}
