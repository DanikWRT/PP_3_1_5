package ru.kata.spring.boot_security.demo.entities;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }
    public Role() {}

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
