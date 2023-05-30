package com.jun.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.jun.jwt.config.jwt.JwtAuthenticationFilter;
import com.jun.jwt.config.jwt.JwtAuthorizationFilter;
import com.jun.jwt.filter.MyFilter3;
import com.jun.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
	
//	private final CorsFilter corsFilter;
	private final UserRepository userRepository;
	
	@Autowired
	private CorsConfig corsConfig; 

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
						.csrf().disable()
						.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						
						.and()
						
						.formLogin().disable()
						.httpBasic().disable()
						
						.apply(new MyCustomDsl())
						
						.and()
						
						.authorizeRequests(authrize -> authrize.antMatchers("/api/v1/user/**")
									.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
									
									.antMatchers("/api/v1/manager/**")
									.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
									
									.antMatchers("/api/v1/admin/**")
									.access("hasRole('ROLE_ADMIN')")
									
									.anyRequest().permitAll())
						
						.build();
						
//				http.addFilterAfter(new MyFilter3(), SecurityContextPersistenceFilter.class);
//				
//				http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 쓰지 않겠다는 뜻
//				
//				.and()
//				
//				.addFilter(corsFilter) //bean에 등록된 모든 필터가 여기를 타고 온다. @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
//				
//				.formLogin().disable()
//				.httpBasic().disable() // http에서 Authorization 방식으로 ID, PW를 전달하는데, 이는 ID,PW가 암호화되지 않은 상태로 전달되기 때문에 위험부담이 크다. 그래서 Token을 전달하는 형식을 취함 
//													// 이를 우리는 Bearer 방식이라고 한다. Token은 노출이 되더라도 로그인할 때 마다 새로운 Token을 서버에서 지급해주기 때문에 (유효시간존재) 위험부담이 적다.
//				.authorizeRequests()
//				.antMatchers("/api/v1/user/**")
//				.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//				
//				.antMatchers("/api/v1/manager/**")
//				.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//				
//				.antMatchers("/api/v1/admin/**")
//				.access("hasRole('ROLE_ADMIN')")
//				
//				.anyRequest().permitAll();
							
			
			
				
	}
	
	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class); //AuthenticationManager를 통해서 로그인을 진행
			http
				.addFilter(corsConfig.coreFilter())
				.addFilter(new JwtAuthenticationFilter(authenticationManager))
				.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
			
		}
		
	}
}
