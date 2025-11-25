package com.example.bloodbankmanagement.dto.service;

import com.example.bloodbankmanagement.dto.pagination.PageRequestDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AssignmentStudentRegisterDto {
    @Data
    public static class AssignmentStudentRegisterSelectListInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Date fromDate;
        private Date toDate;
        private String status;
        private String createUser;
        private PageRequestDto pageRequestDto;
    }

    @Data
    public static class AssignmentStudentRegisterListInfo {
        private long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long studentMapInstructorId;
        private String studentName;
        private String instructorName;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AssignmentStudentRegisterSelectInfo {
        private Long assignmentStudentRegisterId;
    }

    @Data
    public static class AssignmentStudentRegisterSelectInfoResponse {
        private Long assignmentStudentRegisterId;
        private String assignmentStudentRegisterName;
        private Long studentMapInstructorId;
        private String studentName;
        private String instructorName;
        private String fileName;
        private String fileType;
        private String status;
        private String createUser;
        private LocalDate createAt;
    }

    @Data
    public static class AssignmentStudentRegisterInsertInfo {
        private String assignmentStudentRegisterName;
        private MultipartFile fileUpload;
        private Long studentMapInstructorId;
    }

    @Data
    public static class AssignmentStudentRegisterUpdateInfo {
        private long assignmentStudentRegisterId;
        private MultipartFile fileUpload;
        private Long studentMapInstructorId;
        private String assignmentStudentRegisterName;
    }

    @Data
    public static class AssignmentStudentRegisterDeleteInfo {
        private List<Long> listData;
    }
}
