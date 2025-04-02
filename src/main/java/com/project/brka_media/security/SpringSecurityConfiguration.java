package com.project.brka_media.security;

import com.project.brka_media.constants.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;



	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors().and()
				.csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests((authorize) -> authorize
				.antMatchers(WebConstants.BASE_ADMIN_URL+"/**").hasRole("ADMIN")
				.antMatchers(WebConstants.BASE_CLIENT_URL+"/**").hasAnyRole("CLIENT","ADMIN")
				.antMatchers(WebConstants.PUBLIC_BASE_URL+"/**","/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").permitAll().anyRequest().authenticated()
		);
		http.addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
	public CorsFilter corsFilter() {

		List<String> originsList = new ArrayList<String>();

		originsList.add("http://localhost:3000");
		originsList.add("https://localhost:3000");
		originsList.add("https://bacchus-shop.herokuapp.com");
		originsList.add("https://bacchus-shop-prodcution.herokuapp.com");
		originsList.add("https://bacchus-shop-stage.herokuapp.com");
		originsList.add("http://www.bacchus-drinks.ch");
		originsList.add("https://www.bacchus-drinks.ch");

		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();

		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true);
		config.setAllowedOrigins(originsList);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
