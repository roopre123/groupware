 package com.jun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity /*스프링 필터 체인에 등록 ==> 스프링 시큐리티 설정 한다*/
//@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	   protected void configure(HttpSecurity http) throws Exception {
	      http.csrf().disable(); //csrf 공격 예방
	      http.authorizeRequests() //인증 요청에 대한 request
	      .antMatchers("/user/**").authenticated() //user 이하는 로그인만해도 쓸 수 있는 기능 //이 페이지는 admin이나 manager 계정이면 들어올 수 있다
	      .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //이 페이지는 admin만 들어올 수 있다
	      .anyRequest().permitAll() //위의 3개의 페이지 이 외에는 아무나 들어올 수 있다
	      .and() //추가
	      .formLogin() //로그인을 할건데
	      .loginPage("/loginForm") //로그인을 할 상황이 생기면 loginForm 페이지에서 로그인을 해라(스프링 부트에서 제공하는 로그인 사용 안하기 위함)
	      .loginProcessingUrl("/login") //로그인을 하면 넘어갈 페이지
	      .defaultSuccessUrl("/"); //로그인 성공하면 해당 권한의 계정 페이지로 이동하도록 admin으로 로그인하면 admin 페이지로 간다
	}
}
