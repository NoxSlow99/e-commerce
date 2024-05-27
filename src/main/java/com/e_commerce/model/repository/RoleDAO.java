package com.e_commerce.model.repository;

import com.e_commerce.model.entity.Role;
import com.e_commerce.model.entity.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {

    List<Role> findRoleByRoleEnumIs(RoleEnum role);
}
