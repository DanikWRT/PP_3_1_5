package ru.kata.spring.boot_security.demo.etities;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
