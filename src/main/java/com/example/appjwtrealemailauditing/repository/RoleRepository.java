package com.example.appjwtrealemailauditing.repository;

import com.example.appjwtrealemailauditing.entity.Role;
import com.example.appjwtrealemailauditing.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
