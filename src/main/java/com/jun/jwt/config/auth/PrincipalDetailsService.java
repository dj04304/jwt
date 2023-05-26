package com.jun.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jun.jwt.model.User;
import com.jun.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8002/login => 여기서 동작을 안함(직접 로그인 커스텀을 해야함(filter))
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService => loadUserByUsername()");
		User userEntity = userRepository.findByUsername(username);
		System.out.println("userEntity: " + userEntity);
		return new PrincipalDetails(userEntity);
	}

}
