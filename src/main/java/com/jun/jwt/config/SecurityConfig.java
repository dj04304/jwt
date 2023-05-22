package com.jun.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 쓰지 않겠다는 뜻
		
		.and()
		
		.addFilter(corsFilter) //bean에 등록된 모든 필터가 여기를 타고 온다. @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
		
		.formLogin().disable()
		.httpBasic().disable()
		.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		
		.anyRequest().permitAll();
		
	}
}
