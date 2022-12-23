package moe.salamanda.salamanda.configs;

import moe.salamanda.salamanda.securities.*;
import moe.salamanda.salamanda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final String DEFAULT_REMEMBER_ME_KEY = "1145141919SENPAI";

    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setCreateTableOnStartup(false);
        jdbcTokenRepository.setDataSource(dataSource);
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
                .antMatchers("/util/**","/add-ons/**","/error","/resources/**","/check-code","/student-checkcode","/teacher-checkcode","/identitycode-check","/auth-code","/logout","/signout.html").permitAll()
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
                .csrf().disable().cors().disable();
        http.rememberMe()
                .key(DEFAULT_REMEMBER_ME_KEY)
                .userDetailsService(userService)
                .tokenRepository(persistentTokenRepository())
                .rememberMeServices(rememberMeServices())
                .tokenValiditySeconds(60*60*24);
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Bean
    public WithAuthenticationProvider provider(){
        WithAuthenticationProvider provider= new WithAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public WithAuthenticationFilter filter(){
        WithAuthenticationFilter authenticationFilter = new WithAuthenticationFilter(successHandler(),failureHandler(),rememberMeServices());
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

    @Bean
    public WithLogoutSuccessHandler logoutSuccessHandler(){return new WithLogoutSuccessHandler();}

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(){return new PersistentTokenBasedRememberMeServices(DEFAULT_REMEMBER_ME_KEY,userService,persistentTokenRepository());}
}
