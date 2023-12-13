package com.example.demo.member;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.member.dto.MemberJoinDto;

@Service
public class MemberService {
	@Autowired
	private MemberDao dao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    public MemberDto create(MemberJoinDto memberJoinDto) {
        Member member = new Member().toEntity(memberJoinDto);
        member.setPwd(passwordEncoder.encode(memberJoinDto.getPwd()));
        member.setRemain(15);

        if (member.getDeptCode() == 0) member.setDeptName("총무팀");
        else if (member.getDeptCode() == 1) member.setDeptName("인사팀");
        else if (member.getDeptCode() == 2) member.setDeptName("법무팀");
        else if (member.getDeptCode() == 3) member.setDeptName("마케팅팀");
        else if (member.getDeptCode() == 4) member.setDeptName("인프라 서비스팀");
        else if (member.getDeptCode() == 5) member.setDeptName("데이터 서비스팀");
        else if (member.getDeptCode() == 6) member.setDeptName("네트워크 서비스팀");

        if (member.getCompanyRank() == 1) member.setCompanyRankName("사원");
        else if (member.getCompanyRank() == 2) member.setCompanyRankName("대리");
        else if (member.getCompanyRank() == 3) member.setCompanyRankName("과장");
        else if (member.getCompanyRank() == 4) member.setCompanyRankName("차장");
        else if (member.getCompanyRank() == 5) member.setCompanyRankName("부장");
        else if (member.getCompanyRank() == 6) member.setCompanyRankName("상무");
        else if (member.getCompanyRank() == 7) member.setCompanyRankName("전무");
        else if (member.getCompanyRank() == 8) member.setCompanyRankName("대표이사");
        else if (member.getCompanyRank() == 9) member.setCompanyRankName("회장");

        Member m = dao.save(member);

        return new MemberDto().toDto(m);
    }
    
    /*public MemberDto getMember(String username) {
        Member m = dao.findByUsername(username).orElse(null);
        if (m == null) {
            // throw new RuntimeException("사용자를 찾을 수 없습니다.");
            System.err.println("사용자를 찾을 수 없습니다.");
            return null;
        }
        return new MemberDto().toDto(m);
    }*/
    
	public MemberDto getMember(String username) {
		//findById(값): pk컬럼 값으로 찾음. 없으면 예외발생. orElse(null)를 붙이면 없으면 null반환
		Member m = dao.findByUsername(username).orElse(null);
		if(m==null) {
			return null;
		}
		return new MemberDto(m.getId(), m.getUsername(), m.getPwd(), m.getName(), m.getEmail(), 
				m.getPhone(), m.getAddress(), m.getCompanyName(), m.getDeptCode(), m.getDeptName(), 
				m.getCompanyRank(), m.getCompanyRankName(), m.getNewNo(), m.getComCall(),
				m.getIsMaster(), m.getStatus(), m.getCstatus(), m.getOriginFname(), m.getThumbnailFname(),
				m.getNewMemNo(), m.getRemain(),m.getMonthMember(), null);
	}
	
    public ArrayList<MemberDto> getAll() {
        ArrayList<Member> list = (ArrayList<Member>) dao.findAll(Sort.by(Sort.Direction.DESC, "companyRank"));
        ArrayList<MemberDto> list2 = new ArrayList<>();
        for (Member m : list) {
            list2.add(new MemberDto(m.getId(), m.getUsername(), m.getPwd(), m.getName(), m.getEmail(), m.getPhone(), m.getAddress(), m.getCompanyName(), m.getDeptCode(), m.getDeptName(), m.getCompanyRank(), m.getCompanyRankName(), m.getNewNo(), m.getComCall(), m.getIsMaster(), m.getStatus(), m.getCstatus(), m.getOriginFname(), m.getThumbnailFname(), m.getNewMemNo(), m.getRemain(),m.getMonthMember(), null));
        }
        return list2;
    }
	
	//추가, 수정
	//jpa의 save(entity): 한줄추가, 전체컬럼수정, 방금 추가/수정한 객체를 반환
	public MemberDto save(MemberDto dto) {
		Member m = dao.save(new Member(dto.getId(), dto.getUsername(), dto.getPwd(), dto.getName(), dto.getEmail(), 
				dto.getPhone(), dto.getAddress(), dto.getCompanyName(), dto.getDeptCode(), dto.getDeptName(), 
				dto.getCompanyRank(), dto.getCompanyRankName(), dto.getNewNo(), dto.getComCall(),
				dto.getIsMaster(), dto.getStatus(), dto.getCstatus(), dto.getOriginFname(), dto.getThumbnailFname(),
				dto.getNewMemNo(), dto.getRemain(),dto.getMonthMember()));
		
//		Member m2 = dao.save(new Member(dto.getId(), dto.getPwd(), dto.getName(), dto.getEmail()));
		return new MemberDto(m.getId(), m.getUsername(), m.getPwd(), m.getName(), m.getEmail(), 
				m.getPhone(), m.getAddress(), m.getCompanyName(), m.getDeptCode(), m.getDeptName(), 
				m.getCompanyRank(), m.getCompanyRankName(), m.getNewNo(), m.getComCall(),
				m.getIsMaster(), m.getStatus(), m.getCstatus(), m.getOriginFname(), m.getThumbnailFname(),
				m.getNewMemNo(), m.getRemain(),m.getMonthMember(), null);
	}

	
	public void delMember(Long id) {
		//deleteById(값): pk 기준으로 삭제
		dao.deleteById(id);
	}
}
