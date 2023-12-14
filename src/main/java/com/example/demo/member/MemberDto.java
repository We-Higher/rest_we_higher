package com.example.demo.member;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDto {//값 전달 용도 클래스
    private Long id;
    private String username; // 회원 ID
    private String pwd;
    private String name; // 이름
    private String email;
    private String phone; // 휴대전화
    private String address; // 거주지
    private String companyName; // 회사명
    private int deptCode; // 부서 코드
    private String deptName; // 부서명
    private int companyRank; // 직급
    private String companyRankName; // 직급명
    private String newNo; // 사번
    private String comCall; // 내선 전화
    private int isMaster; // 관리자 여부. 0: 일반, 1: Master
    private int status; // 사원 근무 여부. 0: 재직 , 1:퇴사
    private int cstatus; // 사원 출퇴근 관리. 0: 출근, 1: 퇴근
    private String originFname; // 프로필 이미지 원본 파일명
    private String thumbnailFname; // 프로필 이미지 썸네일 파일명
    private int newMemNo; // 임시 테이블 사번
    private int remain; //연차 잔여일
    private int monthMember;
    private MultipartFile f;
    // TODO multipart 추가

    public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .pwd(member.getPwd())
                .name(member.getName())
                .email(member.getEmail())
                .phone(member.getPhone())
                .address(member.getAddress())
                .companyName(member.getCompanyName())
                .deptCode(member.getDeptCode())
                .deptName(member.getDeptName())
                .companyRank(member.getCompanyRank())
                .companyRankName(member.getCompanyRankName())
                .newNo(member.getNewNo())
                .comCall(member.getComCall())
                .isMaster(member.getIsMaster())
                .status(member.getStatus())
                .cstatus(member.getCstatus())
                .originFname(member.getOriginFname())
                .thumbnailFname(member.getThumbnailFname())
                .newMemNo(member.getNewMemNo())
                .remain(member.getRemain())
                .monthMember(member.getMonthMember())
                .build();
    }

    public static MemberDto of(Member member) {
        return new MemberDto(
                member.getId(),
                member.getUsername(),
                member.getPwd(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress(),
                member.getCompanyName(),
                member.getDeptCode(),
                member.getDeptName(),
                member.getCompanyRank(),
                member.getCompanyRankName(),
                member.getNewNo(),
                member.getComCall(),
                member.getIsMaster(),
                member.getStatus(),
                member.getCstatus(),
                member.getOriginFname(),
                member.getThumbnailFname(),
                member.getNewMemNo(),
                member.getRemain(),
                member.getMonthMember(),
                null
        );
    }
}
