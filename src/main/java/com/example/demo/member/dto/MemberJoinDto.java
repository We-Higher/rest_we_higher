package com.example.demo.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberJoinDto {
//    private Long id;
    private String username; // 회원 ID
    private String pwd;
    private String name; // 이름
    private String email;
    private String phone; // 휴대전화
    private String address; // 거주지
    private String companyName; // 회사명
    private int deptCode; // 부서 코드
//    private String deptName; // 부서명
    private int companyRank; // 직급
//    private String companyRankName; // 직급명
    private String newNo; // 사번
    private String comCall; // 내선 전화
    private int isMaster; // 관리자 여부. 0: 일반, 1: Master
    private int status; // 사원 근무 여부. 0: 재직 , 1:퇴사
//    private int cstatus; // 사원 출퇴근 관리. 0: 출근, 1: 퇴근
//    private String originFname; // 프로필 이미지 원본 파일명
//    private String thumbnailFname; // 프로필 이미지 썸네일 파일명
//    private int newMemNo; // 임시 테이블 사번
//    private int remain; //연차 잔여일
    // TODO multipart 추가

//    public MemberJoinDto toMemberJoinDto (Member member) {
//        return MemberJoinDto.builder()
//                .username(member.getUsername())
//                .pwd(member.getPwd())
//                .name(member.getName())
//                .email(member.getEmail())
//                .phone(member.getPhone())
//                .address(member.getAddress())
//                .companyName(member.getCompanyName())
//                .deptCode(member.getDeptCode())
//                .companyRank(member.getCompanyRank())
//                .newNo(member.getNewNo())
//                .comCall(member.getComCall())
//                .isMaster(member.getIsMaster())
//                .status(member.getStatus())
//                .build();
//    }

}
