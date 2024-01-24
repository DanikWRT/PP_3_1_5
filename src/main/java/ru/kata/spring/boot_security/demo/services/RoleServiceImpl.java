package ru.kata.spring.boot_security.demo.services;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

@Transactional(readOnly = true)
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getListRoles() {
        return roleRepository.findAll(Sort.by("name"));
    }
    @Transactional
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
