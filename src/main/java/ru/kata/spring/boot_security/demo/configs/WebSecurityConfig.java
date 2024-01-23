package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import ru.kata.spring.boot_security.demo.security.DatabaseUserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private DatabaseUserDetailService databaseUserDetailService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Autowired
    public void setCustomUserDetailService(DatabaseUserDetailService databaseUserDetailService) {
        this.databaseUserDetailService = databaseUserDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                //.antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/login")
                .permitAll();
    }

    //**Конфигурирование  bcrypt пароля, чтобы хранить пароль в базе в зашифрованном виде,
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //**Провайдер для сверки пароля, пароль проверяется с использованием хэша пароля

    //Здесь мы реализовали daoAuthenticationProvider, с использованием логики из
//    customUserDetailService
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(databaseUserDetailService);
        return authenticationProvider;
    }
//    @Bean для thymeleaf-extras-springsecurity4, для быстрого получения ролей или пользователей
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new SpringSecurityDialect()); // Enable use of "sec"
        return templateEngine;
}

}