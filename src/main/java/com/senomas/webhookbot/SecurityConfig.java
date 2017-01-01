package com.senomas.webhookbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.senomas.boot.security.LoginFilter;
import com.senomas.boot.security.TokenAuthenticationProvider;
import com.senomas.boot.security.service.AuthUserService;
import com.senomas.boot.security.service.TokenService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment environment;

	@Autowired
	TokenService tokenService;

	@Autowired
	AuthUserService authUserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable()
				.addFilterBefore(new LoginFilter(environment, tokenService, authUserService),
						BasicAuthenticationFilter.class)
				.antMatcher("/**").authorizeRequests().and().authorizeRequests().antMatchers("/api/**").authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().headers()
				.frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
		// @formatter:on
	}

	@Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
        authManagerBuilder.authenticationProvider(new TokenAuthenticationProvider(tokenService));
    }
}
