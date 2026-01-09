package com.example.bloodbankmanagement.service.authorization;


import com.example.bloodbankmanagement.common.untils.ERole;
import com.example.bloodbankmanagement.entity.Role;
import com.example.bloodbankmanagement.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public Set<Role> getRole(String roleRequest){
        Set<Role> roles = new HashSet<>();
        if(null == roleRequest){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }else {
                switch (roleRequest){
                    case "3":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "2":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    case "4":
                        Role criticalRole = roleRepository.findByName(ERole.ROLE_CRITICAL)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(criticalRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        break;
            }
        }
        return roles;
    }
}
