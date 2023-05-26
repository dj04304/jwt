package com.jun.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.jwt.config.auth.PrincipalDetails;
import com.jun.jwt.model.User;

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
		
		try {
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			ObjectMapper om = new ObjectMapper(); // JSON 데이터를 파싱해주는 Object
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 return된다.
			Authentication authentication =
					authenticationManager.authenticate(authenticationToken);
			
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println("Complete Login: " + principalDetails.getUser().getUsername()); //로그인이 정상적으로 되었다는 뜻
			//authenticaiton 객체가 session영역에 저장을 해야하고 그 방법이 return 해주면 되는 것이다.
			// 굳이 return을 하는 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는것이다.
			// JWT 토큰을 사용하면서 세션을 만드는 이유는 권한처리때문에 Session을 넣어주는것이다.
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed Login : Please Checking Your Username Or Password");
		}
		
		return null;
	}
	
	// attemptAuthentication 가 먼저 실행되고, 인증이 정상적으로 되었다면, successfulAuthentication가 실행된다.
	// JWT 토큰을 만들어서 request 요청한 사용자에게 JWT토큰을 response해주면 된다.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication Operate: to Complete Authentication");
		System.out.println("Welcome to Login!");
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// RSA방식은 아니고 Hash암호방식 (RSA는 공개키와 서버키 두개를 가지고 있어야 한다. Hash 암호방식이 더 선호된다.)
		String jwtToken = JWT.create()
				.withSubject("jun Token") //토큰 이름
				.withExpiresAt(new Date(System.currentTimeMillis()+(60000 * 10))) // 토큰 만료시간
				.withClaim("id", principalDetails.getUser().getId()) //비공개클레임, 내가 원하는 key value값을 넣어줌
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("jun")); // Secret 값은 나만 알아야하는 서버의 고유 값 임의로 "jun"으로 함
				
		response.addHeader("Authorization", "Bearer " + jwtToken); //header에 담겨서 사용자에게 응답됨
	}
	
}
