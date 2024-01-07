package ru.kata.spring.boot_security.demo.etities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
@Data //Все основные геттеры и сеттеры от ломбок, реализация Equals, hashcode и.т.д...
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username",unique = true)
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов длиной")
    private String username;

    private String password;
    private String email;
    private String firstName;
    private String lastName;


    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;


    //getPassword и getUsername реализованы в ломбок
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles(); //getRoles нам сделал ломбок по приватному полю roles
    }
    // Пока не реализуем Срок годности аккаунта, блокировку или включени/выключение
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
