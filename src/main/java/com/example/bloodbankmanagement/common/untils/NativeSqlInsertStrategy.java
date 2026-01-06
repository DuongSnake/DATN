package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.dto.service.UserDto;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableAutoConfiguration
@Order(1)
public class NativeSqlInsertStrategy  implements UserEntityPermissionBulkInsertStrategy {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder;

    public NativeSqlInsertStrategy(JdbcTemplate jdbcTemplate, PasswordEncoder encoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
    }

    @Override
    public void bulkInsert(List<UserDto.UploadFileRegisterUserInfo> permissions) {
        String sql = "INSERT INTO users (email, phone, full_name, identity_card, address, note, password, username) VALUES (?, ?, ?, ?,?, ?, ?, ?)";
        String defaultPassword = encoder.encode("ktx2024");
        jdbcTemplate.batchUpdate(sql, permissions, permissions.size(), (ps, permission) -> {
            ps.setString(1, permission.getEmail());
            ps.setString(2, permission.getPhone());
            ps.setString(3, permission.getFullName());
            ps.setString(4, permission.getIdentityCard());
            ps.setString(5, permission.getAddress());
            ps.setString(6, permission.getNote());
            ps.setString(6, permission.getNote());
            ps.setString(7, defaultPassword);
            ps.setString(8, permission.getFullName());
        });
    }
}
