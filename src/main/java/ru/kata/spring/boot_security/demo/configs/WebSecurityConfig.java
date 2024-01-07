package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private CustomUserDetailService customUserDetailService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }
    @Autowired
    public void setCustomUserDetailService(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/authenticated/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                //.antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/")
                .permitAll();
    }

    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$0eu1paEfErPJbSM.OwWgQOQN5ziONfrVrfc1ZAgiDAIV3Mkblphby")
//                .roles("USER")
//                .build();
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
//            jdbcUserDetailsManager.deleteUser(user.getUsername());
//        }
//        jdbcUserDetailsManager.createUser(user);
//        return jdbcUserDetailsManager;
//    }

//**Конфигурирование  bcrypt пароля, чтобы хранить пароль в базе в зашифрованном виде,

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //**Провайдер для сверки пароля, пароль проверяется с использованием хэша пароля

    //Здесь мы реализовали daoAuthenticationProvider, с использованием логики из
//    customUserDetailService
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailService);
        return authenticationProvider;
    }
}