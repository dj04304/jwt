package com.jun.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3  implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		// 토큰: cos 이걸 만들어 줘야 함.  id, pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 이를 응답해준다.
		// 요청할 때 마다 header에 Authorization에 value값으로 토큰이 넘어온다.
		// 이때 토큰이 넘어온다면 내가 만든 토큰이 맞는지만 검증하면 된다. ( RSA, HS256 에서 토큰이 맞는지만 인증하는 방식)
				if(req.getMethod().equals("POST")) {
					System.out.println("request POST");
					String headerAuth = req.getHeader("Authorization");
					System.out.println(headerAuth);
					System.out.println("cos filter3");
					
					if(headerAuth.equals("cos")) {
						chain.doFilter(req, res);
					}else {
						PrintWriter out = res.getWriter();
						out.println("Auth error");
					}
				}
			
		chain.doFilter(req, res); //계속 프로세스를 진행하기 위해 chain에 넘김
		
	}
}
