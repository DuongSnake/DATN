package com.example.bloodbankmanagement.repository;


import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    @Query("select u from Role u")
    List<Role> getAllRole();
}
