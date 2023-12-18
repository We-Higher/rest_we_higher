package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import com.example.demo.member.dto.MemberJoinDto;

@SpringBootApplication
public class RestWeHire {

	public static void main(String[] args) {
		SpringApplication.run(RestWeHire.class, args);
	}
	
    @Bean
    public CommandLineRunner initData(MemberService memberService) {
        return (args) -> {
            if (memberService.getMember("admin") == null) {
                MemberDto memberDto = memberService.create(MemberJoinDto.builder()
                        .username("admin")
                        .pwd("1234")
                        .name("관리자")
                        .email("admin@email.com")
                        .phone("010-1234-5678")
                        .address("서울")
                        .companyName("We-Hire")
                        .deptCode(1)
                        .companyRank(9)
                        .newNo("115284")
                        .comCall("02-3415-2108")
                        .isMaster(1)
                        .status(0)
                        .build());
            }
        };
    }
}
