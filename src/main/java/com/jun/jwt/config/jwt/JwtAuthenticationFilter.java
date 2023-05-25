package com.jun.jwt.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있다.
// /login 요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter 가 동작함

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수 
	// 여기서 ID, PW를 확인하면 된다.
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		System.out.println("JwtAuthenticationFilter: try login..");
		
		// 1. username, password 를 받아서
		// 2, 정상인지 로그인 시도를 해본다. authenticationManager 로 로그인 시도를 하면 
		// PrinciaplDetailsService가 호출되면서 loadUserByUsername() 함수가 실행된다.
		
		// 3. PrincipalDetails 를 세션에 담고 (권한관리를 위해 세션에 담는다.)
		// 4. JWT토큰을 만들어서 응답해주면 된다.
		return super.attemptAuthentication(request, response);
	}
	
}
