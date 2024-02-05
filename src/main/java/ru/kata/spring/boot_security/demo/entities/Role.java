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

    public String getWithoutPrefix() { //для JS, чтобы отображать без префикса, рубит "ROLE_"
        return name.substring(5);
    }
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
