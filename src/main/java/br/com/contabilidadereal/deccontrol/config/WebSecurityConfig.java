package br.com.contabilidadereal.deccontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.contabilidadereal.deccontrol.config.security.PesonalizarAccessDeniedHandler;
import br.com.contabilidadereal.deccontrol.service.ImplementUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable().authorizeRequests()
		.antMatchers(HttpMethod.GET, "/").permitAll()
		.antMatchers("/solicitacao/countErro**").permitAll()
		.antMatchers("/solicitacao/countLiberada**").permitAll()
		.antMatchers("/api/consultaPendente").permitAll()
		.antMatchers("/forgot_password**").permitAll()
		.antMatchers("/reset_password**").permitAll()
		.antMatchers("/solicitacao/processar").hasRole("ADMIN")
		.antMatchers("/usuarios/**").hasRole("ADMIN")
		.antMatchers("/declaracao/novo**").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/").failureUrl("/login-error").permitAll()
        .and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll() 
			.logoutSuccessUrl("/").deleteCookies("deccontrol-remember")
		.and()
		.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
		.and()
		.httpBasic()
	    .and()
        .rememberMe()
            .key("unique-and-secret")
            .rememberMeCookieName("deccontrol-remember")
            .tokenValiditySeconds(24 * 60 * 60);;
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
	}

	@Override
	public void configure(WebSecurity web) throws java.lang.Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/vendor/**");
	}
	
	@Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new PesonalizarAccessDeniedHandler();
    }
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new ImplementUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	 @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
	         
	        return authProvider;
	    }
}
