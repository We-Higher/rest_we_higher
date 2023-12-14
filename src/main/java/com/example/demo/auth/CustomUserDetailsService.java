package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDao;
import com.example.demo.member.MemberDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	MemberDao dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member m = dao.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("not found Id : " + username));
		System.out.println("detail service:"+m);
		return new SecurityMember(m);
	}
}