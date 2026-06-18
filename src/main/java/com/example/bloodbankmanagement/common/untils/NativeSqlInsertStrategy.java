package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.dto.service.ScoreAssignmentDto;
import com.example.bloodbankmanagement.dto.service.UserDto;
import com.example.bloodbankmanagement.dto.service.student.StudentDto;
import com.example.bloodbankmanagement.entity.Student;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
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
        String sql = "INSERT INTO users (email, phone, full_name, identity_card, address, note, password, username, role_id, status) VALUES (?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
        String defaultPassword = encoder.encode(CommonUtil.DEFAULT_PASSWORD);
        jdbcTemplate.batchUpdate(sql, permissions, permissions.size(), (ps, permission) -> {
            ps.setString(1, permission.getEmail());
            ps.setString(2, permission.getPhone());
            ps.setString(3, permission.getFullName());
            ps.setString(7, defaultPassword);
            ps.setString(8, permission.getFullName());
            ps.setLong(9, permission.getRoleId());
            ps.setString(10, "1");
        });
    }

    @Override
    public void bulkInsertUser(List<Student> permissions, Long roleId, String passwordDefaultEncrypt) {
        LocalDate nowDate =  LocalDate.now();
        String sql = "INSERT INTO users (email, phone, full_name, password, username, status, status_send_mail, create_at, create_user, role_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, permissions, permissions.size(), (ps, permission) -> {
            ps.setString(1, permission.getEmail());
            ps.setString(2, permission.getPhone());
            ps.setString(3, permission.getFullName());
            ps.setString(4, passwordDefaultEncrypt);
            ps.setString(5, permission.getEmail());
            ps.setString(6, "1");
            ps.setString(7, CommonUtil.NO_VALUE);
            ps.setDate(8, Date.valueOf(nowDate));
            ps.setString(9, "bulkInsertUser");
            ps.setLong(10, roleId);
        });
    }

    @Override
    public void bulkInsertStudent(List<StudentDto.UploadFileRegisterStudentInfo> permissions) {
        LocalDate nowDate =  LocalDate.now();
        String registerUser = CommonUtil.getUsernameByToken();
        //Insert list student with status "have account login" is false
        String sql = "INSERT INTO student (email, phone, full_name, address, major_name, total_lesson_debt, status_done_debt, note, status, status_have_account_login, create_at, create_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, permissions, permissions.size(), (ps, permission) -> {
            ps.setString(1, permission.getEmail());
            ps.setString(2, permission.getPhone());
            ps.setString(3, permission.getFullName());
            ps.setString(4, permission.getAddress());
            ps.setString(5, permission.getMajorName());
            ps.setInt(6, permission.getTotalLessonDebt());
            ps.setString(7, permission.getStatusLessonDebt());
            ps.setString(8, permission.getNote());
            ps.setString(9, CommonUtil.STATUS_USE);
            ps.setString(10, CommonUtil.NO_VALUE);
            ps.setDate(11, Date.valueOf(nowDate));
            ps.setString(12, registerUser);
        });
    }

    @Override
    public void bulkInsertScoreAssignment(List<ScoreAssignmentDto.UploadFileScoreAssignmentInfo> permissions) {
        LocalDate nowDate =  LocalDate.now();
        String registerUser = CommonUtil.getUsernameByToken();
        //Insert list student with status "have account login" is false
        String sql = "INSERT INTO score_assignment (assignment_register_info_id, score_instructor, score_examiner, status, create_at, create_user) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, permissions, permissions.size(), (ps, permission) -> {
            ps.setLong(1, permission.getAssignmentId());
            ps.setDouble(2, Double.valueOf(permission.getScoreInstructor()));
            ps.setDouble(3, Double.valueOf(permission.getScoreExaminer()));
            ps.setString(4, CommonUtil.STATUS_USE);
            ps.setDate(5, Date.valueOf(nowDate));
            ps.setString(6, registerUser);
        });
    }
}
