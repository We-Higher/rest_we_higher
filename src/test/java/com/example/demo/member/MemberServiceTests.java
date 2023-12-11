package com.example.demo.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.dto.MemberJoinDto;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService service;

    @Transactional
    @Rollback(false)
    @Test
    @DisplayName("일반 회원가입")
    public void 일반_회원가입() {
        for (int i = 2; i < 30; i++) {
            service.create(MemberJoinDto.builder()
                    .username("user" + i)
                    .pwd("1234")
                    .name("회원" + i)
                    .email("user" + i + "@email.com")
                    .phone("010-1234-5678")
                    .address("경기")
                    .companyName("We-Hire")
                    .deptCode(i % 6 + 1)
                    .companyRank(i % 9 + 1)
                    .newNo("2023" + i)
                    .comCall("02-3415-2108")
                    .isMaster(0)
                    .status(0)
                    .build());
        }
    }
}
