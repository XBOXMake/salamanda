package moe.salamanda.salamanda.configs;

import moe.salamanda.salamanda.securities.*;
import moe.salamanda.salamanda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class securityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //private AuthenticationDetailsSource<HttpServletRequest, WithWebAuthenticationDetails> authenticationDetailsSource;
    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/students/**").hasRole("STUDENT")
                .antMatchers("/teachers/**").hasRole("TEACHER")
                .antMatchers("/admins/**").hasRole("ADMIN")
                .antMatchers("/index.html").hasAnyAuthority("ADMIN","STUDENT","TEACHER")
                .antMatchers("/add-ons/**","/error","/resources/**","/check-code","/student-checkcode","/teacher-checkcode","/identitycode-check","/auth-code").permitAll()
                .antMatchers("/login.html","/login","/register","/forget").anonymous()
                .anyRequest().authenticated()
                .and()
                .addFilter(filter())
                .formLogin()
                .loginPage("/login.html")
                .passwordParameter("password")
                .usernameParameter("username")
                .loginProcessingUrl("/login")
                .failureHandler(failureHandler())
                .successHandler(successHandler())
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
        http.rememberMe()
                .userDetailsService(userService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60*60*24);
    }

    @Bean
    public WithAuthenticationProvider provider(){
        WithAuthenticationProvider provider= new WithAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public WithAuthenticationFilter filter(){
        WithAuthenticationFilter authenticationFilter = new WithAuthenticationFilter(successHandler(),failureHandler());
        authenticationFilter.setAuthenticationManager(manager());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager manager(){
        return new ProviderManager(Collections.singletonList(provider()));
    }

    @Bean
    public WithAuthentcationSuccessHandler successHandler(){
        return new WithAuthentcationSuccessHandler();
    }

    @Bean
    public WithAuthenticationFailureHandler failureHandler(){
        return new WithAuthenticationFailureHandler();
    }
}
