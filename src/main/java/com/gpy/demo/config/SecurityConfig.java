package com.gpy.demo.config;
import com.gpy.demo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private MyUserDetailsService userDetailService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        //使用BCcypt加密
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        // 将 DataSource 设置到 PersistentTokenRepository
        persistentTokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表
       // persistentTokenRepository.setCreateTableOnStartup(true);
        return persistentTokenRepository;
    }
//权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/index","/publish","/publish/others","/system_Message","/user_Message","/mypublish","/myabutment","/index/detail","/updatestart").hasRole("user")
                .antMatchers("/test").hasRole("admin");
//
                http.formLogin().loginPage("/loginstart").loginProcessingUrl("/login").successForwardUrl("/tologin")
                .and()
                .logout().logoutSuccessUrl("/index");

               http.csrf().disable();
               http.rememberMe()
                      .rememberMeParameter("remember-me")
                      .tokenRepository(persistentTokenRepository())
                      .tokenValiditySeconds(3600*24*7)   //Token过期时间
                      .userDetailsService(userDetailService);
    }
    //认证
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth
                // 从数据库读取的用户进行身份认证
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }

}
